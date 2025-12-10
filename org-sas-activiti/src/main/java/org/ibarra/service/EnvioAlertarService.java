package org.ibarra.service;

import jakarta.annotation.PostConstruct;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.ibarra.conf.AppProps;
import org.ibarra.dto.HistoricoTramiteDto;
import org.ibarra.dto.ServidorDatos;
import org.ibarra.dto.TaskModelTramite;
import org.ibarra.dto.Usuario;
import org.ibarra.entity.HistoricoTramite;
import org.ibarra.entity.HistoricoTramiteObservacion;
import org.ibarra.entity.TareaNotificadaTramite;
import org.ibarra.entity.TipoTramiteAlerta;
import org.ibarra.repository.HistoricoTramiteObservacionRepo;
import org.ibarra.repository.HistoricoTramiteRepo;
import org.ibarra.repository.PersistableRepository;
import org.ibarra.util.Utils;
import org.ibarra.util.model.BusquedaDinamica;
import org.ibarra.util.model.Correo;
import org.ibarra.util.model.EstadoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EnvioAlertarService {

    private Logger LOG = Logger.getLogger(this.getClass().getSimpleName());

    @Autowired
    private AppProps appProps;
    private ProcessEngine processEngine;
    @Autowired
    private RestService restService;
    @Autowired
    private HistoricoTramiteRepo historicoTramiteRepo;
    @Autowired
    private PersistableRepository persistableRepository;
    private Boolean tareaEnEjecucion = false;
    @Autowired
    private BusquedaService busquedaService;
    @Autowired
    private HistoricoTramiteObservacionRepo historicoTramiteObservacionRepo;

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(value = "servidorDatos", key = "#assignee")
    public ServidorDatos getServidorDatosFromCache(String assignee) {
        Cache cache = cacheManager.getCache("servidorDatos");
        ServidorDatos datos = cache.get(assignee, ServidorDatos.class);
        if (datos == null) {
            datos = (ServidorDatos) restService.restPOST(appProps.getUrlTalentoHumano() + "servidor/user/" + assignee, null, null, ServidorDatos.class);
            if (datos != null) {
                cache.put(assignee, datos);
            }
        }
        return datos;
    }


    @PostConstruct
    public void init() {
        processEngine = ProcessEngines.getDefaultProcessEngine();
    }

    @Scheduled(cron = "0 0/10 6-18 * * *")
    @Transactional(readOnly = true)
    public void checkDueDates() {
        if (!appProps.esProd() || Boolean.TRUE.equals(tareaEnEjecucion)) {
            return;
        }

        try {
            tareaEnEjecucion = true;

            LocalDateTime unDiaAntes = LocalDateTime.now().minusDays(1);
            Date unDiaAntesDate = Date.from(unDiaAntes.atZone(ZoneId.systemDefault()).toInstant());

            int pageSize = 100;
            int currentPage = 0;

            TaskQuery baseQuery = processEngine.getTaskService()
                    .createTaskQuery()
                    .active()
                    .taskDueAfter(unDiaAntesDate)
                    .orderByTaskDueDate()
                    .asc();

            while (true) {
                List<Task> tasks = baseQuery
                        .listPage(currentPage * pageSize, pageSize);

                if (tasks.isEmpty()) {
                    break;
                }

                tasks.parallelStream()
                        .filter(this::enviarAlerta)
                        .forEach(this::enviarCorreo);
                System.out.println("Enviado alerta de tareas con fecha de vencimiento mayor a " + unDiaAntesDate
                        + " en pagina: " + currentPage + " de " + tasks.size() + " tareas.");
                currentPage++;
            }
        } finally {
            tareaEnEjecucion = false;
        }
    }


    @Async
    @Transactional
    public void enviarCorreo(Task task) {
        try {
            HistoricoTramite historicoTramite = historicoTramiteRepo.findByIdProceso(task.getProcessInstanceId());
            if (historicoTramite == null) {
                return;
            }

            TipoTramiteAlerta alerta = obtenerAlertaTramite(historicoTramite);
            if (alerta == null || !Boolean.TRUE.equals(alerta.getActivarNotificacion())) {
                return;
            }

            List<TareaNotificadaTramite> notificaciones = obtenerNotificacionesPrevias(historicoTramite, task);
            if (!debeEnviarNotificacion(notificaciones, alerta, task)) {
                return;
            }

            Correo correo = crearCorreo(task, historicoTramite);
            configurarDestinatarioCorreo(correo, task);
            enviarYRegistrarNotificacion(correo, task, historicoTramite);

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al enviar correo", e);
        }
    }

    @Cacheable(value = "alertasTramite", key = "#historicoTramite.id")
    public TipoTramiteAlerta obtenerAlertaTramite(HistoricoTramite historicoTramite) {
        BusquedaDinamica bd = new BusquedaDinamica();
        bd.setEntity(TipoTramiteAlerta.class.getSimpleName());
        bd.setUnicoResultado(true);
        bd.setFilters(new LinkedHashMap<>());
        bd.getFilters().put("tipoTramite.id", historicoTramite.getTipoTramite().getId());
        bd.getFilters().put("estado", EstadoType.ACTIVO);
        return (TipoTramiteAlerta) busquedaService.findAllDinamic(bd.getEntity(), bd);
    }

    @Cacheable(value = "alertasTramite", key = "#historicoTramite.id")
    public List<TareaNotificadaTramite> obtenerNotificacionesPrevias(HistoricoTramite historicoTramite, Task task) {
        BusquedaDinamica db = new BusquedaDinamica();
        db.setEntity(TareaNotificadaTramite.class.getSimpleName());
        db.setFilters(new LinkedHashMap<>());
        db.setOrders(new LinkedHashMap<>());
        db.getOrders().put("id", "desc");
        db.getFilters().put("tramite", historicoTramite.getId());
        db.getFilters().put("idTarea", task.getId());
        db.getFilters().put("estado", true);
        return persistableRepository.findAllDinamic(db);
    }

    private boolean debeEnviarNotificacion(List<TareaNotificadaTramite> notificaciones, TipoTramiteAlerta alerta, Task task) {
        if (Utils.isEmpty(notificaciones)) {
            return true;
        }

        if (Utils.isNotEmpty(notificaciones) && Boolean.TRUE.equals(alerta.getRepetirNotificacion())) {
            return verificarRepeticionNotificacion(notificaciones.get(0), alerta);
        }

        return verificarActivacionPrevia(task, alerta);
    }

    private boolean verificarRepeticionNotificacion(TareaNotificadaTramite notificacion, TipoTramiteAlerta alerta) {
        Date fechaNueva = Utils.sumarDias(notificacion.getFechaCreacion(),
                alerta.getTiempoRepetirNotificacion() == null ? 2 : alerta.getTiempoRepetirNotificacion());
        return compararFechas(fechaNueva);
    }

    private boolean verificarActivacionPrevia(Task task, TipoTramiteAlerta alerta) {
        try {
            Date fechaNueva = Utils.sumarDias(task.getDueDate(), alerta.getTiempoActivacionPrevia() * -1);
            return compararFechas(fechaNueva);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean compararFechas(Date fecha) {
        return Utils.dateFormatPattern("yyyy-MM-dd HH:mm", fecha)
                .equalsIgnoreCase(Utils.dateFormatPattern("yyyy-MM-dd HH:mm", new Date()));
    }

    private Correo crearCorreo(Task task, HistoricoTramite historicoTramite) {
        Correo correo = new Correo();
        correo.setAsunto("Tiene una tarea pendiente por completar del tramite: " + historicoTramite.getTramite());
        correo.setMensaje(String.format("La tarea %s esta proxima a finalizar %s fue asiganda el %s",
                task.getName(),
                Utils.dateFormatPattern("yyyy-MM-dd HH:mm:ss", task.getDueDate()),
                Utils.dateFormatPattern("yyyy-MM-dd HH:mm:ss", task.getCreateTime())));
        return correo;
    }

    private void configurarDestinatarioCorreo(Correo correo, Task task) {
        if ("prod".equalsIgnoreCase(appProps.getAmbiente())) {
            ServidorDatos servidorDatos = getServidorDatosFromCache(task.getAssignee());
            if (servidorDatos != null) {
                correo.setDestinatario(servidorDatos.getCorreoInstitucional() != null ?
                        servidorDatos.getCorreoInstitucional() : servidorDatos.getCorreoPersonal());
            }
        } else {
            correo.setDestinatario(appProps.getCorreoPrueba());
        }
    }

    private void enviarYRegistrarNotificacion(Correo correo, Task task, HistoricoTramite historicoTramite) {
        try {
            restService.restPOST(appProps.getUrlCorreo() + "enviarCorreo", null, correo, Correo.class);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al enviar correo", e);
        }

        TareaNotificadaTramite notificacion = crearNotificacionTarea(correo, task, historicoTramite);
        persistableRepository.save(notificacion);
    }

    private TareaNotificadaTramite crearNotificacionTarea(Correo correo, Task task, HistoricoTramite historicoTramite) {
        TareaNotificadaTramite notificacion = new TareaNotificadaTramite();
        notificacion.setDestinatario(correo.getDestinatario());
        notificacion.setAsunto(correo.getAsunto());
        notificacion.setMensaje(correo.getMensaje());
        notificacion.setTramite(historicoTramite.getId());
        notificacion.setIdTarea(task.getId());
        notificacion.setEstado(true);
        notificacion.setFechaCreacion(new Date());
        notificacion.setUsuarioCreacion("procesoAutomatico");
        notificacion.setEstadoVisto(EstadoType.PENDIENTE.name());
        notificacion.setOrigen("backend");
        return notificacion;
    }

    private boolean enviarAlerta(Task task) {
        return !new Date().after(task.getDueDate());
    }

    @Async
    public void enviarCorreoEdicDuedate(TaskModelTramite task) {
        try {
            Optional<HistoricoTramite> historicoTramiteop = historicoTramiteRepo.findById(task.getIdTramite());
            if (historicoTramiteop.isPresent()) {
                HistoricoTramite historicoTramite = historicoTramiteop.get();
                System.out.println("Enviando correo: " + task.getId() + " usuario " + task.getAssignee());
                Correo correo = new Correo();
                correo.setAsunto("Modificación de fecha del trámite: " + historicoTramite.getTramite());
                correo.setMensaje("La tarea " + task.getName() + " se ha cambiado la fecha maxima permitia a " + Utils.dateFormatPattern("yyyy-MM-dd HH:mm:ss", task.getDueDate()) + " fue asiganda a " + task.getAssignee());
                if ("prod".equalsIgnoreCase(appProps.getAmbiente())) {
                    ServidorDatos servidorDatos = this.getServidorDatosFromCache(task.getAssignee());
                    if (servidorDatos != null) {
                        if (servidorDatos.getCorreoInstitucional() != null) {
                            correo.setDestinatario(servidorDatos.getCorreoInstitucional());
                        } else {
                            correo.setDestinatario(servidorDatos.getCorreoPersonal());
                        }
                    }
                } else {
                    correo.setDestinatario(appProps.getCorreoPrueba());
                }
                try {
                    restService.restPOST(appProps.getUrlCorreo() + "enviarCorreo", null, correo, Correo.class);
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Enviar correo ", e);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Enviar correo ", e);
        }
    }


    @Async
    public void enviarCorreoTareaAsignada(Task task, HistoricoTramiteDto historicoTramite) {
        try {
            if (task == null) {
                System.out.println("No se puede enviar correo a una tarea nula");
                return;
            }
            Usuario us = null;
            if (task.getAssignee() == null) {
                System.out.println("No se puede enviar correo a un usuario nulo de la tarea con id: " + task.getId());
                return;
            }
            ServidorDatos servidorDatos = (ServidorDatos) restService.restPOST(appProps.getUrlAdministrativo() + "servidor/user/" + task.getAssignee(), null, null, ServidorDatos.class);
            if (servidorDatos != null) {
                System.out.println("URL>>>" + appProps.getUrlSeguridad() + "user/findPorPersona/" + servidorDatos.getPersonaID());
                us = (Usuario) restService.restPOST(appProps.getUrlSeguridad() + "user/findPorPersona/" + servidorDatos.getPersonaID(), null, null, Usuario.class);
            }
            String ultimaObs = "";
            System.out.println("Enviando correo: " + task.getId() + " usuario " + task.getAssignee() + " persona: " + servidorDatos.getPersonaID());
            if (us != null && us.getNotificarCorreo()) {
                HistoricoTramiteObservacion ob = historicoTramiteObservacionRepo.findFirstByTramite_idOrderByIdDesc(historicoTramite.getId());
                if (ob != null) {
                    ultimaObs = ob.getObservacion();
                }
                System.out.println("*** Enviando correo: " + task.getId() + " usuario " + task.getAssignee());
                Correo correo = new Correo();
                correo.setAsunto("Tiene una tarea asiganda del tramite: " + historicoTramite.getTramite());
                correo.setMensaje("La tarea " + task.getName() + " fue asignada el día " + Utils.dateFormatPattern("yyyy-MM-dd HH:mm:ss", new Date()) + " y tiene como fecha maxima de completación " + Utils.dateFormatPattern("yyyy-MM-dd HH:mm:ss", task.getDueDate()) + "\n" + "Observación: " + ultimaObs);
                if ("prod".equalsIgnoreCase(appProps.getAmbiente())) {
                    if (servidorDatos != null) {
                        if (servidorDatos.getCorreoInstitucional() != null) {
                            correo.setDestinatario(servidorDatos.getCorreoInstitucional());
                        } else {
                            correo.setDestinatario(servidorDatos.getCorreoPersonal());
                        }
                    }
                } else {
                    correo.setDestinatario(appProps.getCorreoPrueba());
                }
                try {
                    restService.restPOST(appProps.getUrlCorreo() + "enviarCorreo", null, correo, Correo.class);
                } catch (Exception e) {
                    //LOG.log(Level.SEVERE, "Enviar correo ", e);99---------------
                    System.out.println("No se podido envia el correo al usuario: " + task.getAssignee() + " al correo " + correo.getDestinatario());
                }
                TareaNotificadaTramite tareaNotificadaTramite = new TareaNotificadaTramite();
                tareaNotificadaTramite.setDestinatario(correo.getDestinatario());
                tareaNotificadaTramite.setAsunto(correo.getAsunto());
                tareaNotificadaTramite.setMensaje(correo.getMensaje());
                tareaNotificadaTramite.setAsunto(correo.getAsunto());
                tareaNotificadaTramite.setTramite(historicoTramite.getId());
                tareaNotificadaTramite.setIdTarea(task.getId());
                tareaNotificadaTramite.setEstado(true);
                tareaNotificadaTramite.setFechaCreacion(new Date());
                tareaNotificadaTramite.setUsuarioCreacion("procesoAutomaticoTareaAsig.");
                tareaNotificadaTramite.setEstadoVisto(EstadoType.PENDIENTE.name());
                tareaNotificadaTramite.setOrigen("backend");
                persistableRepository.save(tareaNotificadaTramite);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Enviar correo ", e);
        }
    }


}