package org.ibarra.service;


import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.ibarra.conf.AppProps;
import org.ibarra.dto.*;
import org.ibarra.dto.flujoDinamico.*;
import org.ibarra.dto.sgm.AclUser;
import org.ibarra.entity.HistoricoTramite;
import org.ibarra.entity.TareaModificadaTramite;
import org.ibarra.entity.TipoTramite;
import org.ibarra.entity.TipoTramiteAlerta;
import org.ibarra.repository.HistoricoTramiteRepo;
import org.ibarra.repository.PersistableRepository;
import org.ibarra.util.Utils;
import org.ibarra.util.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EdicionDiagramaService {
    private Logger LOG = Logger.getLogger(this.getClass().getSimpleName());
    @Autowired
    private AppProps appProps;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TipoTramiteService tipoTramiteService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private HistoricoTramiteRepo historicoTramiteRepo;
    @Autowired
    private HistoricoTramiteService tramiteService;
    @Autowired
    private PersistableRepository persistableRepository;
    @Autowired
    private EnvioAlertarService envioAlertarService;

    @Autowired
    private BusquedaService busquedaService;
    @Autowired
    private ReporteService reporteService;
    @Autowired
    private RestService restService;
    @Autowired
    private PersonaService personaService;

    public EdicionDiagramaService() {
//        this.processEngine = ProcessEngines.getDefaultProcessEngine();
    }


    public File getFileBpmn(String fileBpmn) {
        try {
            System.out.println(fileBpmn);
            String ruta = appProps.getPathBpmn() + fileBpmn;
            ruta = ruta.replace("//", "/");
            ruta = ruta.replace("procesos/procesos", "procesos");
            File file = ResourceUtils.getFile(ruta);
//            System.out.println(file.getAbsolutePath());
            return file;
        } catch (FileNotFoundException e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public BpmnModel parseBpmnFile(String filePath) throws Exception {
        Path path = Path.of(filePath);
        try {
            BpmnXMLConverter converter = new BpmnXMLConverter();
            try (InputStream inputStream = Files.newInputStream(path)) {
                XMLStreamReader xmlStreamReader = XMLInputFactory.newDefaultFactory().createXMLStreamReader(inputStream);
                return converter.convertToBpmnModel(xmlStreamReader);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, path.toString(), e);
            return null;
        }
    }

    public void addTimerToTask(BpmnModel model, String taskId, String duration) {
        FlowElement element = model.getMainProcess().getFlowElement(taskId);
        if (element instanceof UserTask userTask) {
            if (userTask.getPriority() == null) {
                userTask.setPriority("50");
            } else {
                if ("".equalsIgnoreCase(userTask.getPriority().trim())) {
                    userTask.setPriority("50");
                }
            }
            BoundaryEvent timerEvent = new BoundaryEvent();
            timerEvent.setId(taskId + "_timer");
            timerEvent.setAttachedToRef(userTask);
            if (userTask.getDueDate() == null) {
                userTask.setDueDate(duration);
                userTask.setBoundaryEvents(List.of(timerEvent));

                TimerEventDefinition timerDefinition = new TimerEventDefinition();
                timerDefinition.setId(taskId + "_timer_duration");
                timerDefinition.setTimeDuration(duration);
                timerEvent.addEventDefinition(timerDefinition);

                model.getMainProcess().addFlowElement(timerEvent);
                GraphicInfo graphicInfo = model.getGraphicInfo(taskId);
                Double hw = 30.00;
                GraphicInfo clone = new GraphicInfo();
                clone.setElement(userTask);
                clone.setExpanded(graphicInfo.getExpanded());
                clone.setHeight(hw);
                clone.setX(Utils.round((graphicInfo.getX() + graphicInfo.getWidth()) - (hw / 2), 1));
                clone.setWidth(hw);
                clone.setY(Utils.round((graphicInfo.getY() + graphicInfo.getHeight()) - (hw / 1.8), 1));
                model.addGraphicInfo(timerEvent.getId(), clone);
            }

        }
    }

    public List<UsuarioTarea> getTaskStatuses(List<Task> tasks) {
        List<UsuarioTarea> statuses = new ArrayList<>();

        for (Task task : tasks) {
            UsuarioTarea status = new UsuarioTarea();
            status.setId(task.getId());

            if (task.isSuspended()) {
                if (task.getClaimTime().before(task.getDueDate())) {
                    status.setState("COMPLETED_ON_TIME");
                } else {
                    status.setState("COMPLETED_LATE");
                }
            } else if (task.getDueDate().before(new Date())) {
                status.setState("OUT_OF_TIME");
            } else if (task.getDueDate().before(Utils.sumarDias(new Date(), 1))) {
                status.setState("NEARING_DEADLINE");
            } else {
                status.setState("ASSIGNED_ON_TIME");
            }

            statuses.add(status);
        }

        return statuses;
    }

    public BpmnModel loadBpmnDiagram(TipoTramiteDto tipoTramiteDto) {
        TipoTramiteDto tramiteDto = tipoTramiteService.consultar(tipoTramiteDto);
        File fileBpmn = getFileBpmn(tramiteDto.getArchivoBpmn());
        try {
            if (fileBpmn == null) {
                return null;
            }
            BpmnModel model = parseBpmnFile(fileBpmn.getAbsolutePath());
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            List<String> userTask = new ArrayList<>(flowElements.size());
            for (FlowElement element : flowElements) {
                if (element instanceof UserTask) {
                    UserTask task = (UserTask) element;
                    if (Utils.isEmptyString(task.getDueDate())) {
                        userTask.add(element.getId());
                    }
                }
            }
            for (String element : userTask) {
                addTimerToTask(model, element, "P2D");
            }
            return model;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] loadBpmnDiagramXml(TipoTramiteDto tipoTramiteDto) {
        TipoTramiteDto tramiteDto = tipoTramiteService.consultar(tipoTramiteDto);
        File fileBpmn = getFileBpmn(tramiteDto.getArchivoBpmn());
        try {
            FileInputStream in = new FileInputStream(fileBpmn.getAbsolutePath());
            byte[] xml = in.readAllBytes();
            return xml;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] loadBpmnDiagramXmlTimer(TipoTramiteDto tipoTramiteDto) {
        try {
            BpmnModel model = loadBpmnDiagram(tipoTramiteDto);
            if (model == null) {
                return null;
            }
            return getModelToBytes(model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getModelToBytes(BpmnModel model) {
        if (model == null) {
            return null;
        }
        try {
            BpmnXMLConverter converter = new BpmnXMLConverter();
            return converter.convertToXML(model);
        } catch (Exception e) {
            System.out.println("Error al convertir xml " + e.getMessage());
            return null;
        }
    }

    public RespuestaWs guardarXml(BpmnXml bpmnXml) {
        try {
//            URL url = ReflectUtil.getResource(bpmnXml.getTipoTramite().getArchivoBpmn());
            File fileBpmn = getFileBpmn(bpmnXml.getTipoTramite().getArchivoBpmn());
            String file = fileBpmn.getAbsolutePath();
            File oldFile = new File(file);
            String fileRename = oldFile.getName();
            fileRename = fileRename.replace(".bpmn", new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + ".bpmn");
            fileRename = oldFile.getParent().concat(File.separator).concat(fileRename);
            System.out.println("Archivo Anterior:  " + fileRename);
            Files.copy(new File(file).toPath(), new FileOutputStream(fileRename));

            BufferedReader in = new BufferedReader(new StringReader(bpmnXml.getXmlNuevo()));
            FileWriter fileWriter = new FileWriter(file);
            long transfer = in.transferTo(fileWriter);

            System.out.println("Nuevo archivo: " + file + " transfer " + transfer);
            fileWriter.flush();
            Thread.sleep(3000);
            System.out.println("Configuracion de Procesos");
            BusquedaDinamica bd = new BusquedaDinamica();
            bd.setEntity(TipoTramiteAlerta.class.getSimpleName());
            bd.setUnicoResultado(true);
            bd.setFilters(new LinkedHashMap<>());
            bd.getFilters().put("tipoTramite.id", bpmnXml.getTipoTramite().getId());
            bd.getFilters().put("estado", EstadoType.ACTIVO);
            TipoTramiteAlerta alerta = (TipoTramiteAlerta) this.busquedaService.findAllDinamic(bd.getEntity(), bd);
            if (alerta == null) {
                alerta = new TipoTramiteAlerta();
                alerta.setEstado(EstadoType.ACTIVO.name());
                alerta.setFechaCreacion(new Date());
                alerta.setFechaModificacion(alerta.getFechaCreacion());
                alerta.setUsuarioCreacion("admin");
                alerta.setUsuarioModificacion("admin");
                alerta.setRepetirNotificacion(bpmnXml.getRepetirNotificacion());
                alerta.setActivarNotificacion(bpmnXml.getActivarNotificacion());
                alerta.setTiempoActivacionPrevia(bpmnXml.getTiempoActivacionPrevia());
                alerta.setRepetirNotificacion(bpmnXml.getRepetirNotificacion());
                alerta.setTipoTramite(new TipoTramite(bpmnXml.getTipoTramite().getId()));
            } else {
                alerta.setFechaModificacion(alerta.getFechaCreacion());
                alerta.setUsuarioModificacion("admin");
                alerta.setRepetirNotificacion(bpmnXml.getRepetirNotificacion());
                alerta.setActivarNotificacion(bpmnXml.getActivarNotificacion());
                alerta.setTiempoActivacionPrevia(bpmnXml.getTiempoActivacionPrevia());
                alerta.setRepetirNotificacion(bpmnXml.getRepetirNotificacion());
            }

            persistableRepository.save(alerta);
            return new RespuestaWs(true, null, "Archivo actualizado correctamente");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return new RespuestaWs(false, null, "Error al modificar archivo bomn");
        }
    }

    public RespuestaWs agregarTimerPorDefecto(String rutaBase64, String periodoDuracion) {
        try {
            String archivo = Utils.decodeBase64(rutaBase64);
            BpmnModel model = parseBpmnFile(archivo);
            if (model == null) {
                return null;
            }
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            List<String> userTask = new ArrayList<>(flowElements.size());
            for (FlowElement element : flowElements) {
                if (element instanceof UserTask) {
                    userTask.add(element.getId());
                }
            }
            for (String element : userTask) {
                addTimerToTask(model, element, periodoDuracion);
            }
            BpmnXMLConverter converter = new BpmnXMLConverter();
            byte[] bytes = converter.convertToXML(model);
            String archivoAnterior = archivo.replace(".bpmn", new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + ".bpmn");
            Files.copy(new File(archivo).toPath(), new FileOutputStream(archivoAnterior));
            BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
            FileWriter fileWriter = new FileWriter(archivo);
            long transfer = in.transferTo(fileWriter);
            fileWriter.flush();
            return new RespuestaWs(true, null, "Proceso completado con exito, byte copiados: " + transfer);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return new RespuestaWs(false, null, e.getMessage());
        }
    }

    public byte[] loadBpmnDiagramXmlTramite(Long idTramite) {
        try {
            Optional<HistoricoTramite> tramiteOpc = this.historicoTramiteRepo.findById(idTramite);
            if (tramiteOpc.isPresent()) {
                HistoricoTramite tramite = tramiteOpc.get();
                HistoricProcessInstance processInstanceId = this.processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(tramite.getIdProceso()).singleResult();
                if (processInstanceId != null) {
                    BpmnModel model = null;
                    if (tramite.getTipoTramite().getId() == 50L) {
                        Servicio sb = new Servicio();
                        sb.setId(tramite.getReferenciaId());
                        Servicio servicio = (Servicio) restService.restPOST(appProps.getUrlVentanillaInterna() + "servicio/solicitud/servicio", null, sb, Servicio.class);
                        System.out.println(sb);
                        BpmnXml bpmnXml = this.generarBpmn(servicio);
                        if (bpmnXml == null) {
                            return null;
                        }
                        return bpmnXml.getXmlAnterior().getBytes(Charset.forName("UTF-8"));
                    } else {
                        model = this.processEngine.getRepositoryService().getBpmnModel(processInstanceId.getProcessDefinitionId());
                    }
                    if (model == null) {
                        return null;
                    }
                    System.out.println("Conversion de xml de tramite " + idTramite);
                    return getModelToBytes(model);
                } else {
                    System.out.println("Instancia de proceso no encontrado " + idTramite + " id Proceso " + tramite.getIdProceso());
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<TaskModelTramite> buscarTarea(String idProcInst, HistoricoTramiteDto tramite) {
        System.out.println("Process Instance " + idProcInst);

        List<HistoricTaskInstance> tasks = new ArrayList<>();

        List<HistoricTaskInstance> tasksPrincipal = this.processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .processInstanceId(idProcInst)
                .orderByTaskCreateTime()
                .asc()
                .list();

        if (Utils.isNotEmpty(tasksPrincipal)) {
            tasks.addAll(tasksPrincipal);
        }

        // Buscar subprocesos en caso de los trámites de Avalúos y Catastros
        List<HistoricProcessInstance> subproceso = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .superProcessInstanceId(idProcInst)
                .list();

        // Agregar tareas de los subprocesos
        if (Utils.isNotEmpty(subproceso)) {
            List<HistoricTaskInstance> tasksSubproceso = this.processEngine.getHistoryService()
                    .createHistoricTaskInstanceQuery()
                    .processInstanceId(subproceso.getFirst().getId())
                    .orderByTaskCreateTime()
                    .asc()
                    .list();

            if (Utils.isNotEmpty(tasksSubproceso)) {
                tasks.addAll(tasksSubproceso);
            }

            tasks.sort(Comparator.comparing(HistoricTaskInstance::getStartTime));
        }

        if (Utils.isNotEmpty(tasks)) {
            List<TaskModelTramite> result = new LinkedList<>();
            Long idTramite = tramite.getId();
            for (HistoricTaskInstance task : tasks) {
                TaskModelTramite f = new TaskModelTramite(task.getId());
                f.setIdTramite(idTramite);
                List<HistoricIdentityLink> linksForTask = processEngine.getHistoryService().getHistoricIdentityLinksForTask(task.getId());
                if (Utils.isNotEmpty(linksForTask)) {
                    List<String> s = new ArrayList<>(linksForTask.size());
                    for (HistoricIdentityLink link : linksForTask) {
                        s.add(link.getUserId());
                    }
                    f.setAssignee(String.join(",", s));
                } else {
                    f.setAssignee(task.getAssignee());
                }
                f.setCategory(task.getCategory());
                if (tramite.getTipoTramite().getId() == 50l) {
                    f.setTaskDefinitionKey(Utils.toCamelCase(task.getName().strip()));
                } else {
                    f.setTaskDefinitionKey(task.getTaskDefinitionKey());
                }
                f.setDescription(task.getDescription());
                f.setName(task.getName());

                // En los trámites de catastros, no se ha establecido tiempo, se van a poner 3 días para cada tarea
                if (task.getDueDate() == null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(task.getStartTime());
                    cal.add(Calendar.DAY_OF_MONTH, 3);
                    f.setDueDate(cal.getTime());
                } else {
                    f.setDueDate(task.getDueDate());
                }

                f.setCreataDate(task.getStartTime());
                f.setEndDate(task.getEndTime());
                f.setId(task.getId());
                f.setPriority(task.getPriority());
                EstadoTarea estadoTarea = EstadoTarea.UNASSIGNED;
                Date fechaActual = new Date();
                // Terminada
                System.out.println(task.getId() + " TaskDefinitionKey " + task.getTaskDefinitionKey() + " name " + task.getName() + " task.getEndTime() " + task.getEndTime() + " task.getDurationInMillis() " + task.getDurationInMillis());
                if (task.getDurationInMillis() != null) {
                    if (task.getEndTime() != null && task.getDueDate() != null) {
                        if (task.getEndTime().before(task.getDueDate())) {
                            estadoTarea = EstadoTarea.COMPLETED_ON_TIME;
                        } else {
                            estadoTarea = EstadoTarea.COMPLETED_LATE;
                        }
                    } else if (task.getDueDate() != null && task.getDueDate().before(fechaActual)) {
                        estadoTarea = EstadoTarea.COMPLETED_LATE;
                    } else if (task.getDueDate() != null && task.getDueDate().before(Utils.sumarDias(fechaActual, 1))) {
                        estadoTarea = EstadoTarea.COMPLETED_LATE;
                    } else {
                        estadoTarea = EstadoTarea.COMPLETED_ON_TIME;
                    }
                } else {
                    if (task.getDueDate() != null) {
                        if (task.getDueDate().before(fechaActual)) {
                            estadoTarea = EstadoTarea.OUT_OF_TIME;
                        } else if (task.getDueDate().before(Utils.sumarDias(fechaActual, 1))) {
                            estadoTarea = EstadoTarea.NEARING_DEADLINE;
                        } else {
                            estadoTarea = EstadoTarea.ASSIGNED_ON_TIME;
                        }
                    } else {
                        estadoTarea = EstadoTarea.NEARING_DEADLINE;
                    }
                }

                f.setEstado(estadoTarea);
                f.setDescripcionEstado(estadoTarea.getDescripcion());
                f.setColor(estadoTarea.getColor());
                f.setTextColor(estadoTarea.getTextColor());

                String[] usuarios = f.getAssignee().split(",");
                for (String usuario : usuarios) {
                    UsuarioDetalle ud;

                    if (usuario.matches("[A-Z]{2}[a-z]+")) { // Usuarios del sistema de catastros
                        BusquedaDinamica b = BusquedaDinamica.builder("AclUser").unicoResultado(true)
                                .where("usuario", usuario)
                                .build();

                        AclUser user = (AclUser) this.restService.restPOST(this.appProps.getUrlBddimi().concat("busquedas/findBy"), null, b, AclUser.class);

                        if (user != null) {
                            ud = new UsuarioDetalle();
                            ud.setUsuario(user.getUsuario());
                            ServidorDatos sd = new ServidorDatos();
                            sd.setApellidos(user.getEnte().getApellidos());
                            sd.setNombres(user.getEnte().getNombres());
                            sd.setIdentificacion(user.getEnte().getIdentificacion());
                            ud.setServidor(sd);
                            f.getUsuarios().add(ud);
                        }

                    } else if (usuario.matches("[a-z]+")) {  // Usuarios de SmartGob
                        ud = personaService.getUsuario(usuario);
                        if (ud != null) {
                            f.getUsuarios().add(ud);
                        }
                    }
                }

                result.add(f);


            }
            return result;
        }
        return null;
    }

    public HistoricoTramiteDto buscarXnumtramite(String idTramite) {
        try {
            return buscarTramite(null, idTramite);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HistoricoTramiteDto buscarTramite(Long idTramite, String numTramite) {
        try {
            HistoricoTramiteDto htd;
            System.out.println("numTramite: "+numTramite);
            if (idTramite != null) {
                htd = tramiteService.consultarXid(idTramite, null);
            } else {
                System.out.println("entra aqui");
                htd = tramiteService.consultarXid(null, numTramite);
            }

           // System.out.println("Tramite id " + idTramite + " relacionar observacion con tareas, getTramite " + htd.getTramite());
            if (htd != null) {
                System.out.println("llegó>>>>>>>>");
                htd.setTaskModelTramites(this.buscarTarea(htd.getIdProceso(), htd));
                if (Utils.isNotEmpty(htd.getTaskModelTramites())) {
                    List<HistoricoTramiteObservacionDto> observacionDtos = this.processService.procesosObservaciones(htd);
                    observacionDtos.sort(Comparator.comparing(HistoricoTramiteObservacionDto::getFechaCreacion));
                    Map<String, HistoricoTramiteObservacionDto> observacionesPorTarea = observacionDtos.stream()
                            .filter(o -> o.getTarea() != null)
                            .collect(Collectors.toMap(
                                    o -> o.getTarea().strip().toLowerCase(),
                                    o -> o,
                                    (existing, replacement) -> existing // Mantener la primera observación en caso de duplicados
                            ));

                    htd.getTaskModelTramites().forEach(task -> {
                        HistoricoTramiteObservacionDto observacion = observacionesPorTarea.get(task.getName().strip().toLowerCase());
                        if (observacion != null) {
                            task.setUltimaObservacion(observacion);
                        }
                    });

//                    for (TaskModelTramite od : htd.getTaskModelTramites()) {
//                        for (HistoricoTramiteObservacionDto o : observacionDtos) {
//                            if (o.getTarea() != null) {
//                                System.out.println("Relacion de observacion con tarea " + o.getTarea());
//                                if (o.getTarea().strip().equalsIgnoreCase(od.getName().strip())) {
//                                    od.setUltimaObservacion(o);
//                                }
//                            }
//                        }
//                    }
                    htd.setObservaciones(observacionDtos);
                }
            }else{
                System.out.println("vacío>>>>>>>>");
            }
            return htd;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public RespuestaWs actualizarTarea(TaskModelTramite task) {
        try {
            HistoricTaskInstance tsk = processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();
            TareaModificadaTramite actualizacion = new TareaModificadaTramite();
            actualizacion.setIdTarea(task.getId());
            actualizacion.setObservacion(task.getObservacion());
            actualizacion.setFechaCreacion(task.getFechaModificacion());
            actualizacion.setUsuarioCreacion(task.getUsuarioModificacion());
            actualizacion.setFechaDuadateAnterior(tsk.getDueDate());
            actualizacion.setFechaDuedate(task.getDueDate());
            actualizacion.setEstado(true);
            System.out.println("Envio a modificar tarea duedate " + task.getId());
            processEngine.getTaskService().setDueDate(task.getId(), task.getDueDate());
            actualizacion = (TareaModificadaTramite) persistableRepository.save(actualizacion);
            try {
                envioAlertarService.enviarCorreoEdicDuedate(task);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "", e);
            }
            return new RespuestaWs(true, Utils.toJson(task), "Duedate de la tarea " + task.getId() + " actualizado correctamente");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return new RespuestaWs(false, null, e.getMessage());
        }
    }

    public void generarDiagrama(HistoricoTramiteDto tramiteDto, String tipo, HttpServletResponse response) {
        try {
            int RESOLUTION_DPI = 800;
            float SCALE_BY_RESOLUTION = RESOLUTION_DPI / 72f;
            float scaledWidth = 252 * SCALE_BY_RESOLUTION;
            float scaledHeight = 144 * SCALE_BY_RESOLUTION;

            scaledWidth = 151 * SCALE_BY_RESOLUTION;
            scaledHeight = 86 * SCALE_BY_RESOLUTION;
            float pixelUnitToMM = new Float(100.4f / RESOLUTION_DPI);

            Random rd = new Random();
            String nombreTemp = rd.longs().toString();
            InputStream svgFileStream = new ByteArrayInputStream(tramiteDto.getSvg().getBytes(StandardCharsets.UTF_8));
            TranscoderInput inputSvgImage = new TranscoderInput(svgFileStream);

            ByteArrayOutputStream pngFileStream = new java.io.ByteArrayOutputStream();
            TranscoderOutput outputPngImage = new TranscoderOutput(pngFileStream);
            PNGTranscoder converter = new PNGTranscoder();
            converter.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);
            converter.addTranscodingHint(PNGTranscoder.KEY_WIDTH, scaledWidth);
            converter.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, scaledHeight);
            //converter.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 1.0f);
            converter.addTranscodingHint(PNGTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, pixelUnitToMM);

            converter.transcode(inputSvgImage, outputPngImage);
            if ("PNG".equalsIgnoreCase(tipo)) {
                response.setContentType("image/png");
                response.setContentLength(pngFileStream.size());
                System.out.println("Imagen en png generada");
                //IOUtils.write(pngFileStream.toByteArray(), response.getOutputStream());
                pngFileStream.writeTo(response.getOutputStream());
                // response.getOutputStream().write(pngFileStream.toByteArray());
            } else {
                System.out.println("Inicia generar reporte ");

                DatosReporte reporte = new DatosReporte();
                reporte.setDataList(Arrays.asList(tramiteDto));
                reporte.setDataSource(true);
                reporte.setFormato(ReporteFormato.PDF.getCodigo());
                reporte.setParametros(new HashMap<>());
                reporte.setNombreReporte("detalleTramite");

                byte[] bytes = reporteService.generarReporte(reporte);
                System.out.println("Export report " + bytes.length);
                PDDocument document = Loader.loadPDF(bytes);
                PDDocumentInformation info = new PDDocumentInformation();
                info.setAuthor("ERP ORIOGAMIEC");
                info.setCreationDate(Calendar.getInstance());
                info.setTitle("Diagrama de Proceso");
                info.setCreator("ERP ORIOGAMIEC");
                document.setDocumentInformation(info);
                InputStream in = new ByteArrayInputStream(pngFileStream.toByteArray());
                BufferedImage bimg = ImageIO.read(in);
                float width = bimg.getWidth();
                float height = bimg.getHeight();
                PDPage page = new PDPage(new PDRectangle(width, height));
                document.addPage(page);
                PDImageXObject imageXObject = PDImageXObject.createFromByteArray(document, pngFileStream.toByteArray(), "digrama.png");
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.drawImage(imageXObject, 0, 0);
                contentStream.close();
                in.close();
                response.setContentType("application/pdf");
                System.out.println("Pdf generado");
                document.save(response.getOutputStream());
                document.close();
            }
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public BpmnXml buscarBpmnXml(TipoTramiteDto tipoTramite) {
        try {
            BpmnXml bpmnXml = new BpmnXml();
            // Busqueda de xml
            byte[] xmlTimer = loadBpmnDiagramXmlTimer(tipoTramite);
            bpmnXml.setXmlAnterior(new String(xmlTimer));
            // Tiempo registrado para el proceso
            BusquedaDinamica b = new BusquedaDinamica("TipoTramiteAlerta");
            b.setUnicoResultado(true);
            b.setFilters(new LinkedHashMap<>());
            b.getFilters().put("estado", "ACTIVO");
            b.getFilters().put("tipoTramite.id", tipoTramite.getId());
            TipoTramiteAlerta alerte = (TipoTramiteAlerta) busquedaService.findAllDinamic(b.getEntity(), b);
            if (alerte != null) {
                bpmnXml.setRepetirNotificacion(alerte.getRepetirNotificacion());
                bpmnXml.setActivarNotificacion(alerte.getActivarNotificacion());
                bpmnXml.setTiempoActivacionPrevia(alerte.getTiempoActivacionPrevia());
                bpmnXml.setTiempoRepetirNotificacion(alerte.getTiempoRepetirNotificacion());
            }
            // Resumen de tareas
            BpmnModel model = loadBpmnDiagram(tipoTramite);
            if (model != null) {
                Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
                bpmnXml.setResumenTareas(new ArrayList<>(flowElements.size()));
                Integer cx = 0;
                for (FlowElement element : flowElements) {
                    if (element instanceof UserTask) {
                        cx++;
                        UserTask ut = (UserTask) element;
                        ResumenTarea resumenTarea = new ResumenTarea(ut.getId(), ut.getName(), ut.getName(), ut.getDueDate());
                        resumenTarea.setIndex(cx);
                        bpmnXml.getResumenTareas().add(resumenTarea);
                    }
                }
            }
            System.out.println(bpmnXml);
            return bpmnXml;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return null;
        }
    }

    public BpmnXml generarBpmn(Servicio servicio) {
        try {
            Long idServicio = servicio.getId();
            BpmnXml bpmnXml = new BpmnXml();
            if (servicio.getNombre() == null) {
                servicio = (Servicio) restService.restPOST(appProps.getUrlVentanillaInterna() + "servicio/listar-id", null, servicio, Servicio.class);
                TipoTramiteDto tipoTramite = new TipoTramiteDto();
                tipoTramite.setId(servicio.getTipoTramite());
                bpmnXml.setTipoTramite(this.tipoTramiteService.consultar(tipoTramite));
            }
            if (Utils.isEmpty(servicio.getTareas())) {
                BusquedaDinamica filterLazy = new BusquedaDinamica("ServicioTarea");
                filterLazy.addSort("orden", "ASC");
                filterLazy.addFilter("servicio.id", servicio.getId());
                filterLazy.setUnicoResultado(false);
                List<ServicioTarea> tar = (List<ServicioTarea>) restService.restPOST(appProps.getUrlVentanillaInterna() + "busquedas/findBy", null, filterLazy, ServicioTarea[].class);

                System.out.println("Listado de tareas: " + (tar == null ? "Nulo" : tar.size()));
                if (Utils.isNotEmpty(tar)) {
                    for (ServicioTarea starea : tar) {
                        System.out.println("Tarea " + starea.getTarea().getTarea());
                        filterLazy = new BusquedaDinamica("ServicioResponsable");
                        filterLazy.setUnicoResultado(false);
                        filterLazy.addFilter("servicioTarea.id", starea.getId());
                        List<ServicioResponsable> responsables = (List<ServicioResponsable>) restService.restPOST(appProps.getUrlVentanillaInterna() + "busquedas/findBy", null, filterLazy, ServicioResponsable[].class);
                        if (Utils.isNotEmpty(responsables)) {
                            starea.getResponsables().addAll(responsables);
                        }

                        filterLazy = new BusquedaDinamica("ServicioTareaRelacion");
                        filterLazy.setUnicoResultado(false);
                        filterLazy.addFilter("servicioTarea.id", starea.getId());
                        filterLazy.addFilter("tipo", Boolean.FALSE);
                        List<ServicioTareaRelacion> relacion = (List<ServicioTareaRelacion>) restService.restPOST(appProps.getUrlVentanillaInterna() + "busquedas/findBy", null, filterLazy, ServicioTareaRelacion[].class);
                        if (Utils.isNotEmpty(relacion)) {
                            starea.getAntecesoras().addAll(relacion);
                        }

                        filterLazy = new BusquedaDinamica("ServicioTareaRelacion");
                        filterLazy.setUnicoResultado(false);
                        filterLazy.addFilter("servicioTarea.id", starea.getId());
                        filterLazy.addFilter("tipo", Boolean.TRUE);
                        relacion = (List<ServicioTareaRelacion>) restService.restPOST(appProps.getUrlVentanillaInterna() + "busquedas/findBy", null, filterLazy, ServicioTareaRelacion[].class);
                        if (Utils.isNotEmpty(relacion)) {
                            starea.getSucesoras().addAll(relacion);
                        }

                    }
                    servicio.setTareas(new ArrayList<>(tar));
                }
            }
            ServidorCargoDto defaultCargo = new ServidorCargoDto(0l, new CargoDto(-1l, servicio.getNombre()));
            boolean porUnidad = false;

            List<ServicioTarea> tareas = new LinkedList<>();
            for (ServicioTarea ta : servicio.getTareas()) {
                if (ta.getResponsables() == null) {
                    ta.setResponsables(new LinkedList<>());
                    ta.getResponsables().add(new ServicioResponsable());
                    ta.getResponsables().get(0).setServidorCargo(defaultCargo);
                }
                List<ServicioResponsable> responsables = ta.getResponsables();
                ta.setResponsables(new LinkedList<>());
                for (ServicioResponsable resp : responsables) {
                    ta.getResponsables().add(resp);
                }
                tareas.add(ta);
            }

            servicio.setTareas(tareas);
            servicio.getTareas().stream().sorted((o1, o2) -> o1.getOrden().compareTo(o2.getOrden()));
            List<UnidadAdministrativaDto> unidades = new LinkedList<>();
            List<CargoDto> cargos = new LinkedList<>();
            HashMap<Long, CargoDto> tareaCargos = new LinkedHashMap<>();

            boolean agregarDefaultCargo = false;
            if (Utils.isNotEmpty(servicio.getTareas())) {
                for (ServicioTarea tarea : servicio.getTareas()) {
                    for (ServicioResponsable srcargo : tarea.getResponsables()) {
                        if (srcargo.getServidor() != null) {
                            try {
                                RespuestaWs rw = (RespuestaWs) restService.restPOST(appProps.getUrlTalentoHumano().concat("servidor/find/servidor/cargo?id=" + srcargo.getServidor()), null, null, RespuestaWs.class);
                                if (rw.getEstado()) {
                                    ServidorDatos sd = (ServidorDatos) Utils.toObjectFromJson(rw.getData(), ServidorDatos.class, "MMM dd, yyyy, hh:mm:ss a");
                                    ServidorCargoDto cargo = sd.getServidorCargos() == null ? defaultCargo : sd.getServidorCargos();
                                    tareaCargos.put(tarea.getId(), cargo.getCargo());
                                    srcargo.setServidorCargo(cargo);
                                    if (!cargos.contains(cargo.getCargo())) {
                                        cargos.add(cargo.getCargo());
                                    }
                                    if (sd.getServidorCargos().getCargo() != null) {
                                        if (!unidades.contains(cargo.getCargo().getUnidad())) {
                                            unidades.add(cargo.getCargo().getUnidad());
                                        }
                                    }
                                } else {
                                    agregarDefaultCargo = true;
                                    srcargo.setServidorCargo(defaultCargo);
                                }
                            } catch (Exception e) {
                                agregarDefaultCargo = true;
                                srcargo.setServidorCargo(defaultCargo);
                                System.out.println("Error al consultar datos del servidor " + srcargo.getServidor() + " Mensaje de error: " + e.getMessage());
                            }
                        } else {
                            agregarDefaultCargo = true;
                            srcargo.setServidorCargo(defaultCargo);
                        }
                        tareaCargos.put(tarea.getId(), srcargo.getServidorCargo().getCargo());
                    }
                }
            }
            if (agregarDefaultCargo) {
                if (!cargos.contains(defaultCargo.getCargo())) {
                    cargos.add(defaultCargo.getCargo());
                }
            }
            double x = 10.0;
            double y = 10.0;
            double width = 1050.0;
            double heightLane = 250;
            double height = (cargos.size() == 0 ? 1 : cargos.size()) * heightLane;
            boolean variosDep = Utils.isNotEmpty(cargos);
            if (porUnidad) {
                height = (unidades.size() == 0 ? 1 : unidades.size()) * heightLane;
                variosDep = Utils.isNotEmpty(unidades);
            }
            if (!variosDep) {
                heightLane = 450;
                height = heightLane;
            }

            String camelCase = Utils.toCamelCase(servicio.getNombre());
            servicio.getTramite();

            BpmnModel model = new BpmnModel();

            Process process = new Process();
            process.setName(servicio.getNombre());
            process.setId(camelCase);

            System.out.println("Tareas a agregar " + servicio.getTareas().size() + " Lanes: " + cargos.size() + " Es por unidad " + porUnidad + " varios dep " + variosDep + "\n");
            // Creamos los lanes para ubicar las tareas
            if (variosDep) {
                double ylane = y;
                double xlane = x + 30.0;
                if (porUnidad) {
                    for (UnidadAdministrativaDto unidad : unidades) {
                        ylane = createLane("lane_" + Utils.toCamelCase(unidad.getNombre()), unidad.getNombre(), process, model, xlane, ylane, width - 30.0, heightLane);
                    }
                } else {
                    for (CargoDto cargo : cargos) {
                        ylane = createLane("lane_" + cargo.getId(), cargo.getNombreCargo(), process, model, xlane, ylane, width - 30.0, heightLane);
                    }
                }
                height = ylane;
            } else {
                String name = servicio.getNombre();
                String id = "lane_" + servicio.getId();
                if (servicio.getDepartamento() != null) {
                    name = servicio.getDepartamento().getDescripcion();
                    id = Utils.toCamelCase(servicio.getDepartamento().getDescripcion());
                }
                createLane(id, name, process, model, x + 30.0, y, width - 30.0, height);
            }
            double heightTask = 50.0;
            double widthTask = 150.0;
            double xtask = 120.0;
            double yTask = 75.0;


            // Agregado Lanes proceso y al pool
            Pool pool = new Pool();
            pool.setId("poolid_" + camelCase);
            pool.setName(servicio.getNombre());
            pool.setValues(process);
            pool.setProcessRef(process.getId());
            model.getPools().add(pool);
            model.addProcess(process);
            generarGraphicInfo(x, y, width, height, pool, null, model, null);

            // Craemos las tareas en cada lane correspondiente
            int indexTask = 0;
            Lane lane = process.getLanes().get(0);
            FlowElement source = addEvent(model, process, lane, true, null);
            GraphicInfo sourceGInfo = model.getGraphicInfo(source.getId());
            xtask = sourceGInfo.getX() + sourceGInfo.getWidth() + 75.0;
            lane = null;
            List<String> userTasks = new ArrayList<>();
            for (ServicioTarea ta : servicio.getTareas()) {
                lane = getLane(ta, variosDep, porUnidad, process, lane, defaultCargo, tareaCargos);
                System.out.println(ta.getId() + " Tarea: " + ta.getTarea().getTarea() + " Orden " + ta.getOrden() + " con lane " + lane.getId());
                UserTask usert = agregarResponsableTarea(ta, lane, process);
                userTasks.add(usert.getId());
                // Agregamos la ubicacion de la tarea
                sourceGInfo = generarGraphicInfo(xtask, yTask, widthTask, heightTask, usert, lane, model, source);
                // Agregamos las notas de la tarea
                agregarAnotacion(ta, usert, sourceGInfo, lane, process, model);
                // Creamos los flow para unir las tareas
                connectFlow(source, usert, model, lane, process);
                if (xtask > width) {
                    xtask = 20.0;
                    yTask = heightTask + 30.0;
                } else {
                    xtask = sourceGInfo.getWidth() + sourceGInfo.getX() + 75.0;
                }
                // Si es la ultima tarea agregamos el end event.
                indexTask = indexTask + 1;
                source = usert;
                System.out.println();
            }
            model.setUserTaskFormTypes(userTasks);
            addEvent(model, process, lane == null ? process.getLanes().get(0) : lane, false, source);

            byte[] m = getModelToBytes(model);
            if (m != null) {
                String xml = new String(m);
                xml = xml.replace(":participant id=\"" + process.getId() + "\"", ":participant id=\"part_" + process.getId() + "\"");
                xml = xml.replace("bpmnElement=\"" + process.getId() + "\"", "bpmnElement=\"part_" + process.getId() + "\"");
                xml = xml.replace("id=\"BPMNShape_" + process.getId() + "\"", "id=\"BPMNShape_part_" + process.getId() + "\"");
                bpmnXml.setXmlAnterior(xml);
                /*m = xml.getBytes();
                File f = new File("C:\\Users\\anava\\OneDrive\\Documents\\Proyectos\\Ibarra\\Erp-Ibarra\\erp-ibarra\\org-ibarra-activiti\\target\\pruebaProceso.bpmn");
                Files.write(f.toPath(), m);*/
            }
            return bpmnXml;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return null;
        }
    }

    private Lane getLane(ServicioTarea ta, boolean variosDep, boolean porUnidad, Process process, Lane lane, ServidorCargoDto defaultCargo, HashMap<Long, CargoDto> tareaCargos) {
        if (variosDep) {
            CargoDto cargo = tareaCargos.containsKey(ta.getId()) ? tareaCargos.get(ta.getId()) : defaultCargo.getCargo();
            String idLane = "lane_" + cargo.getId();
            if (porUnidad) {
                if (cargo.getUnidad() != null) {
                    UnidadAdministrativaDto unidad = cargo.getUnidad();
                    idLane = "lane_" + Utils.toCamelCase(unidad.getNombre());
                }
            }
            for (Lane l : process.getLanes()) {
                if (l.getId().equalsIgnoreCase(idLane)) {
                    lane = l;
                    break;
                }
            }
        } else {
            lane = process.getLanes().get(0);
        }
        return lane;
    }

    private UserTask agregarResponsableTarea(ServicioTarea ta, Lane lane, Process process) {
        UserTask usert = new UserTask();
        //usert.setId("task_" + ta.getId());
        usert.setId(Utils.toCamelCase(ta.getTarea().getTarea().strip()));
        usert.setName(ta.getOrden() + ".- " + ta.getTarea().getTarea());
        if (Utils.isNotEmpty(ta.getResponsables())) {
            if (ta.getResponsables().size() == 1) {
                ServicioResponsable responsable = ta.getResponsables().get(0);
                usert.setAssignee(responsable.getResponsable());
            } else {
                List<String> candi = new ArrayList<>(ta.getResponsables().size());
                for (ServicioResponsable responsable : ta.getResponsables()) {
                    candi.add(responsable.getResponsable());
                }
                usert.setCandidateUsers(candi);
            }
        }
        usert.setParentContainer(process);
        // Agregamos la tarea al proceso
        lane.getFlowReferences().add(usert.getId());
        process.addFlowElement(usert);
        process.addFlowElementToMap(usert);
        return usert;
    }

    private SequenceFlow connectFlow(FlowElement source, FlowElement target, BpmnModel model, Lane lane, Process process) {
        SequenceFlow sf = new SequenceFlow();
        sf.setId("flow_" + source.getId() + "_to_" + target.getId());
        sf.setSourceRef(source.getId());
        sf.setSourceFlowElement(source);

        sf.setTargetRef(target.getId());
        sf.setTargetFlowElement(target);

        GraphicInfo sourceGraphic = model.getGraphicInfo(source.getId());
        GraphicInfo targetGraphic = model.getGraphicInfo(target.getId());

        Grafico centerXSource = Utils.centerGraphic(sourceGraphic.getX(), sourceGraphic.getY(), sourceGraphic.getWidth(), sourceGraphic.getHeight()); // x
        Grafico centerXTarget = Utils.centerGraphic(targetGraphic.getX(), targetGraphic.getY(), targetGraphic.getWidth(), targetGraphic.getHeight()); // y

        List<GraphicInfo> infos = new LinkedList<>();
        Grafico puntoSalida = Utils.calcularPuntoCercano(sourceGraphic, targetGraphic);
        Double px1 = puntoSalida.getX();
        Double py1 = puntoSalida.getY();

        sf.getWaypoints().add(px1.intValue());
        sf.getWaypoints().add(py1.intValue());
        infos.add(generarGraphicInfo(px1, py1, 0, 0, sf, null, model, null));

        Integer angulo = Utils.calcularAngulo(centerXTarget, centerXSource);
        System.out.println(sf.getId() + " Angulo " + angulo + " source " + source.getId() + " target " + target.getId());
        if (!(puntoSalida.getDireccion().equals(Grafico.Direccion.RECTO_HACIA_ABAJO)
                || puntoSalida.getDireccion().equals(Grafico.Direccion.RECTO_HACIA_ARRIBA)) &&
                angulo >= 20) {
            boolean recto = Math.abs(centerXTarget.getY() - centerXSource.getY()) < 20;
            if (!recto) {
                Double cx1 = centerXSource.getX();
                Double cy1 = centerXSource.getY() + (Math.abs(puntoSalida.getY() - puntoSalida.getPuntoLLegada().getY()) / 1.8);
                sf.getWaypoints().add(cx1.intValue()); // x
                sf.getWaypoints().add(cy1.intValue()); // y
                infos.add(generarGraphicInfo(cx1, cy1, 0, 0, sf, null, model, null));
                cx1 = centerXTarget.getX();
                sf.getWaypoints().add(cx1.intValue()); // x
                sf.getWaypoints().add(cy1.intValue()); // y
                infos.add(generarGraphicInfo(cx1, cy1, 0, 0, sf, null, model, null));
            }
        }
        //Grafico puntoLlegada = Utils.calcularPuntoCercano(sourceGraphic, targetGraphic);
        Double px2 = puntoSalida.getPuntoLLegada().getX();
        Double py2 = puntoSalida.getPuntoLLegada().getY();
        sf.getWaypoints().add(px2.intValue()); // x
        sf.getWaypoints().add(py2.intValue()); // y
        infos.add(generarGraphicInfo(px2.intValue(), py2.intValue(), 0, 0, sf, null, model, null));

        // Agregar a los respectivos contenedores
        if (process != null) {
            process.addFlowElement(sf);
        }

        model.getFlowLocationMap().put(sf.getId(), infos);
        model.addFlowGraphicInfoList(sf.getId(), infos);

        if (source instanceof UserTask) {
            UserTask usource = (UserTask) source;
            usource.getOutgoingFlows().add(sf);
        }
        if (target instanceof UserTask) {
            UserTask utarget = (UserTask) target;
            utarget.getIncomingFlows().add(sf);
        }
        return sf;
    }

    /**
     * Si el Lane es nulo, se agregan las coordenadas que fueron pasadas.
     *
     * @param x         Posicion en x
     * @param y         Posicion en Y
     * @param width     Ancho
     * @param height    Alto
     * @param element   Elemento al cual se le va a crear shape
     * @param container Contenedor de la tarea de usuario
     * @param model
     * @return
     */
    private GraphicInfo generarGraphicInfo(double x, double y, double width, double height, BaseElement element, Lane container, BpmnModel model, BaseElement previo) {
        GraphicInfo gInfo = new GraphicInfo();
        gInfo.setX(Utils.round(x, 1));
        gInfo.setY(Utils.round(y, 1));
        gInfo.setWidth(Utils.round(width, 1));
        gInfo.setHeight(Utils.round(height, 1));
        boolean laneIsEmpty = false;
        boolean agregarY = true;
        if (container != null) {
            String last = container.getId();
            GraphicInfo info = model.getGraphicInfo(last);
            GraphicInfo containerGra = model.getGraphicInfo(container.getId());
            double x1 = (info.getX() + 100.0);
            double y1 = info.getY() + 65.0;
            if ((element instanceof UserTask)) {
                int compoents = 0;
                ArrayList<String> referencesLane = new ArrayList<>(container.getFlowReferences());
                boolean remove = referencesLane.remove(element.getId());
                laneIsEmpty = referencesLane.isEmpty();
                boolean isStartEvent = false;
                if (!laneIsEmpty) {
                    for (int i = referencesLane.size() - 1; i >= 0; i--) {
                        //for (int i = 0; i < referencesLane.size(); i++) {
                        FlowElement flowElement = container.getParentProcess().getFlowElement(container.getFlowReferences().get(i));
                        if (flowElement != null) {
                            if (!isStartEvent) {
                                isStartEvent = flowElement instanceof StartEvent;
                            }
                            if (flowElement instanceof UserTask || flowElement instanceof StartEvent || flowElement instanceof Gateway) {
                                compoents++;
                                last = flowElement.getId();
                                agregarY = false;
                                info = model.getGraphicInfo(last);
                                if (info.getX() >= x1) {
                                    x1 = (info.getX() + info.getWidth()) + 100.0;
                                    y1 = info.getY();
                                }
                                //}
                            }
                        }
                    }
                    Grafico grafico = new Grafico(x1, y1, gInfo.getWidth(), gInfo.getHeight());
                    boolean inst = Utils.verficarIntercepcion(grafico, model);

                    if ((x1 + width) >= (containerGra.getWidth() + containerGra.getX())) {
                        y1 = y1 + height + 150.0;
                        x1 = (x1 - width * 2.5);
                    }

                    if (inst) {
                        x1 = grafico.getIntersection().getX() + 120.0;
                    }
                    gInfo.setX(Utils.round(x1, 1));
                    gInfo.setY(Utils.round(y1, 1));

                } else {
                    info = model.getGraphicInfo(previo.getId());
                    GraphicInfo infoLane = model.getGraphicInfo(container.getId());
                    double newX = info.getX();
                    double newy = infoLane.getY() + 120.0;
                    Grafico grafico = new Grafico(newX, newy, gInfo.getWidth(), gInfo.getHeight());
                    boolean inst = Utils.verficarIntercepcion(grafico, model);
                    if (inst) {
                        newX = newX + grafico.getX() + 95.0 + (width / 2);
                    }
                    gInfo.setX(Utils.round(newX, 1));
                    gInfo.setY(Utils.round(newy, 1));
                }
            }
        }

        gInfo.setElement(element);
        if (element instanceof UserTask) {
            System.out.println(element.getId() + " Fin GraphicInfo Tarea id " + element.getId() + " x " + gInfo.getX() + " y " + gInfo.getY() + " width " + gInfo.getWidth() + " height " + gInfo.getHeight() + " container vacio " + laneIsEmpty);
        }
        gInfo.setExpanded(true);
        if (!(element instanceof SequenceFlow)) {
            model.addGraphicInfo(element.getId(), gInfo);
        }
        return gInfo;
    }

    private double createLane(String id, String nombre, Process process, BpmnModel model, double xlane, double ylane, double width, double heightLane) {
        Lane lane = new Lane();
        lane.setName(nombre);
        lane.setId(id);
        lane.setParentProcess(process);
        // Ubicamos el lane en el diagrama
        generarGraphicInfo(xlane, ylane, width, heightLane, lane, null, model, null);
        ylane = heightLane + ylane;
        //Agregamos el lane al proceso
        process.getLanes().addLast(lane);
        return ylane;
    }

    private void agregarAnotacion(ServicioTarea servicioTarea, UserTask usert, GraphicInfo sourceGInfo, Lane lane, Process process, BpmnModel model) {
        String descripcion = servicioTarea.getTarea().getDescripcion();
        if (descripcion == null) {
            return;
        }
        if (descripcion.trim().length() == 0) {
            return;
        }
        TextAnnotation ta = new TextAnnotation();
        ta.setId("anotacion_for_" + usert.getId());
        descripcion = descripcion.replaceAll("\\n+", " ");
        if (descripcion.trim().length() > 103) {
            descripcion = descripcion.substring(0, 103) + "...";
        }
        ta.setText(descripcion);
        lane.getFlowReferences().add(ta.getId());
        process.addArtifact(ta);
        GraphicInfo info = generarGraphicInfo(sourceGInfo.getX() + 30, sourceGInfo.getY() - 60, sourceGInfo.getWidth() * 1.60, 50, ta, null, model, null);
        model.addGraphicInfo(ta.getId(), info);
        Association sf = new Association();
        sf.setId("flow_anotacion_" + usert.getId() + "_to_" + ta.getId());
        sf.setSourceRef(usert.getId());
        sf.setTargetRef(ta.getId());
        if (lane != null) {
            lane.getFlowReferences().add(sf.getId());
        }
        if (process != null) {
            process.addArtifact(sf);
        }
        model.getGlobalArtifacts().add(ta);
    }

    private FlowElement addEvent(BpmnModel model, Process process, Lane lane, boolean start, FlowElement usert) {
        double heightWidthEvent = 30.0;
        double xtask = 0.0;
        double yTask = 0.0;
        if (start) {
            GraphicInfo csi = model.getGraphicInfo(lane.getId());
            xtask = csi.getX() + 90.0;
            yTask = csi.getY() + 100.0;
        } else {
            GraphicInfo sourceInfo = model.getGraphicInfo(usert.getId());
            yTask = ((sourceInfo.getY() + (sourceInfo.getHeight() / 1.6)) - heightWidthEvent / 1.8) - 6.0;
            xtask = sourceInfo.getX() + sourceInfo.getWidth() + 80.0;
        }
        if (start) {
            StartEvent startEvent = new StartEvent();
            startEvent.setId("start_0");
            startEvent.setName("Inicio Tramite");
            startEvent.setParentContainer(process);
            process.addFlowElement(startEvent);
            lane.getFlowReferences().add(startEvent.getId());
            generarGraphicInfo(xtask + heightWidthEvent, yTask, heightWidthEvent, heightWidthEvent, startEvent, lane, model, null);
            return startEvent;
        } else {
            EndEvent endEvent = new EndEvent();
            endEvent.setId("end_0");
            endEvent.setName("Fin Tramite");
            endEvent.setParentContainer(process);
            process.addFlowElement(endEvent);
            lane.getFlowReferences().add(endEvent.getId());
            generarGraphicInfo(xtask + heightWidthEvent, yTask, heightWidthEvent, heightWidthEvent, endEvent, null, model, null);

            SequenceFlow sff = connectFlow(usert, endEvent, model, lane, process);
            return endEvent;
        }
    }
}