package org.ibarra.service;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.ibarra.conf.AppProps;
import org.ibarra.dto.*;
import org.ibarra.dto.Process;
import org.ibarra.entity.HistoricoTramite;
import org.ibarra.entity.HistoricoTramiteObservacion;
import org.ibarra.entity.Persona;
import org.ibarra.entity.TipoTramite;
import org.ibarra.mapper.HistoricoTramiteMapper;
import org.ibarra.mapper.HistoricoTramiteObservacionMapper;
import org.ibarra.mapper.PersonaMapper;
import org.ibarra.repository.HistoricoTramiteObservacionRepo;
import org.ibarra.repository.HistoricoTramiteRepo;
import org.ibarra.repository.PersonaRepository;
import org.ibarra.util.Constantes;
import org.ibarra.util.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoricoTramiteService {
    @Autowired
    private AppProps appProps;
    @Autowired
    private HistoricoTramiteRepo repository;
    @Autowired
    private HistoricoTramiteObservacionRepo observacionRepo;
    @Autowired
    private RestService restService;
    @Autowired
    private HistoricoTramiteMapper tramiteMapper;
    @Autowired
    private HistoricoTramiteObservacionMapper observacionMapper;
    @Autowired
    private HistoricoTramiteRepo historicoTramiteRepo;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private PersonaMapper personaMapper;
    @Autowired
    private PersonaService personaService;

    public HistoricoTramiteDto consultarXtramite(String tramite) {
        HistoricoTramite ht = repository.findFirstByTramiteOrderByIdDesc(tramite);
        if (ht != null) {
            HistoricoTramiteDto dto = tramiteMapper.toDto(ht);
            if (dto != null) {
                if (ht.getSolicitante() != null) {
                    try {
                        PersonaDto persona = personaService.consultarXid(ht.getSolicitante().getId());
                        if (persona != null && persona.getNumIdentificacion() != null) {
                            dto.setSolicitante(persona);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return dto;
            }
        }
        return null;
    }

    public HistoricoTramiteDto consultarHistoricoTramite(String processInstance) {
        HistoricoTramite ht = repository.findByIdProceso(processInstance);
        if (ht != null) {
            HistoricoTramiteDto dto = tramiteMapper.toDto(ht);
            if (dto != null) {
                if (ht.getSolicitante() != null) {
                    Persona persona = personaRepository.getReferenceById(ht.getSolicitante().getId());
                    PersonaDto personaDto = personaMapper.toDto(persona);

                    if (personaDto != null && personaDto.getNumIdentificacion() != null) {
                        RespuestaWs respuesta = new RespuestaWs();
                        respuesta.setData(persona.getNumIdentificacion());
                        dto.setSolicitante(personaDto);
                    }
                }
                return dto;
            }
        }
        return null;
    }


    public HistoricoTramiteDto consultarHistoricoTramite(HistoricoTramite ht) {

        HistoricoTramiteDto dto = tramiteMapper.toDto(ht);
        if (dto != null) {
            if (ht.getSolicitante() != null) {
                PersonaDto persona = personaMapper.toDto(ht.getSolicitante());
                if (persona != null && persona.getNumIdentificacion() != null) {
                    dto.setSolicitante(persona);
                }
            }
            return dto;
        }

        return null;
    }


    //@Transactional
    public HistoricoTramite crearTramite(Process process) {
        System.out.println("crearTramite: " + process);
        try {
            HistoricoTramite historicoTramite;
            Integer periodo = Utils.getAnio(new Date());
            String codigo = process.getTramite();
            if (codigo == null) {
                historicoTramite = new HistoricoTramite();
                if (process.getPeriodo() != null) {
                    periodo = process.getPeriodo();
                }
                RespuestaWs secuencia = (RespuestaWs) restService.restPOST(appProps.getUrlAdministrativo() + "configuraciones/secuencia/generarSecuencia", null, new RespuestaWs(process.getTipoTramite().getAbreviatura()), RespuestaWs.class);
                if (secuencia != null && secuencia.getEstado()) {
                    codigo = process.getTipoTramite().getAbreviatura() + "-" + Utils.completarCadenaConCeros(secuencia.getMensaje(), 4) + "-" + periodo;
                }
            } else {
                historicoTramite = repository.findFirstByTramiteOrderByIdDesc(codigo); // PARA PODER REINGRESAR UN TRAMITE
                if (historicoTramite == null) {
                    historicoTramite = new HistoricoTramite();
                }
            }
            System.out.println("codigo: " + codigo);
            if (codigo != null) {
                Persona persona = personaRepository.getReferenceById(process.getSolicitante());

                historicoTramite.setTramite(codigo);
                historicoTramite.setSolicitante(persona);
                historicoTramite.setTipoTramite(new TipoTramite(process.getTipoTramite().getId()));
                historicoTramite.setConcepto(process.getConcepto());
                historicoTramite.setDocumento(Boolean.FALSE);
                historicoTramite.setEntregado(Boolean.FALSE);
                historicoTramite.setFecha(new Date());
                historicoTramite.setFechaIngreso(new Date());
                historicoTramite.setEstado(Boolean.TRUE);
                historicoTramite.setReferencia(process.getReferencia());
                historicoTramite.setReferenciaId(process.getReferenciaId());
                //System.out.println(""+historicoTramite.setReferenciaId());
                historicoTramite.setFechaEntrega(process.getFechaMaximiFinalizacion());
                historicoTramite.setPeriodo(periodo);
                historicoTramite.setUsuarioCreacion(process.getUsuario());
                historicoTramite = repository.save(historicoTramite);
                return historicoTramite;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actualizarTramite(Long id, ProcessInstance processInstance, Tarea tarea) {
        if (Utils.isNotEmptyString(tarea.getObservacion())) {
            tarea.setObservacion(tarea.getObservacion());
        } else {
            tarea.setObservacion(Constantes.inicioTramite);
        }
        HistoricoTramite historicoTramite = repository.getReferenceById(id);
        historicoTramite.setIdProceso(processInstance.getId());
        historicoTramite.setIdProcesoTemp(processInstance.getProcessDefinitionId());
        historicoTramite = repository.save(historicoTramite);
        guardarObservacion(id, tarea.getObservacionUsuario() != null ? tarea.getObservacionUsuario() : historicoTramite.getUsuarioCreacion(), tarea);
        /// return historicoTramite;
    }

    public HistoricoTramiteObservacionDto guardarObservacion(Tarea tarea) {

        if (tarea.getTramite().getId() == null && Utils.isNotEmptyString(tarea.getTramite().getTramite())) {
            HistoricoTramite historicoTramite = repository.findFirstByTramiteOrderByIdDesc(tarea.getTramite().getTramite());
            tarea.getTramite().setId(historicoTramite.getId());

        }

        HistoricoTramiteObservacion observacion = guardarObservacion(tarea.getTramite().getId(), tarea.getObservacionUsuario(), tarea);
        return observacionMapper.toDto(observacion);
    }

    public HistoricoTramiteObservacion guardarObservacion(Long historicoTramiteId, String usuario, Tarea tarea) {
        try {
            HistoricoTramiteObservacion observacion = new HistoricoTramiteObservacion();
            observacion.setObservacion(tarea.getObservacion() == null ? "Continuar Trámite" : tarea.getObservacion());
            observacion.setObservacionPublica(tarea.getObservacionPublica() == null ? "T" : tarea.getObservacionPublica());
            observacion.setTarea(tarea.getTaskName());
            observacion.setEstado(Boolean.TRUE);
            observacion.setTramite(new HistoricoTramite(historicoTramiteId));
            observacion.setFechaCreacion(new Date());
            observacion.setUsuarioCreacion(usuario);
            return observacionRepo.save(observacion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<HistoricoTramiteObservacionDto> observaciones(Long tramite) {
        try {
            List<HistoricoTramiteObservacion> observaciones = observacionRepo.findAllByTramite_IdAndEstadoTrueOrderByFechaCreacionAsc(tramite);
            if (Utils.isNotEmpty(observaciones)) {
                List<HistoricoTramiteObservacionDto> observacionesDto = observacionMapper.toDto(observaciones);
                for (HistoricoTramiteObservacionDto obs : observacionesDto) {
                    if (esCedulaValida(obs.getUsuarioCreacion()) || esRucValido(obs.getUsuarioCreacion())) {
                        System.out.println("es CedulaRUCValida " + obs.getUsuarioCreacion());
                        continue;
                    }
                    UsuarioDetalle ud = personaService.getUsuario(obs.getUsuarioCreacion());
                    if (ud != null) {
                        if (ud.getServidor() != null) {
                            if (ud.getServidor().getDireccionAdministrativa() != null) {
                                obs.setUsuarioCreacion(ud.getServidor().getDireccionAdministrativa().getNombre() + "\n" +
                                        ud.getServidor().getNombres() + " " + ud.getServidor().getApellidos() + "\n" +
                                        "(" + ud.getUsuario() + ")");
                            }
                        }
                    }
                }
                return observacionesDto;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<HistoricoTramite> listaTramitePeriodo(TipoTramiteDto tipoTramiteDto, Integer periodo) {
        return repository.findAllByTipoTramite_IdAndPeriodoOrderByIdDesc(tipoTramiteDto.getId(), periodo);
    }

    public HistoricoTramiteDto consultarXid(Long id, String numTramite) {
        HistoricoTramite ht;
        if (id != null) {
            ht = repository.getReferenceById(id);
        } else {
            ht = repository.findFirstByTramiteOrderByIdDesc(numTramite);
        }

        if (ht != null) {
            HistoricoTramiteDto dto = tramiteMapper.toDto(ht);
            if (dto != null) {
                try {
                    if (ht.getSolicitante() != null) {
                        try {
                            Persona persona = personaRepository.getReferenceById(ht.getSolicitante().getId());
                            //PersonaDto persona = (PersonaDto) restService.restPOST(appProps.getUrlAdministrativo() + "cliente/consultarSolicitante/" + ht.getSolicitante().getId(), null, null, PersonaDto.class);
                            if (persona != null && persona.getNumIdentificacion() != null) {
                                PersonaDto dtoPersona = personaMapper.toDto(persona);
                                dto.setSolicitante(dtoPersona);
                            }
                        } catch (Exception e) {
                            System.out.println("No se pudo consultar el solicitante. " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error de coneccion al servicio de administrativo");
                }
                return dto;
            }
        }
        return null;
    }

    public HistoricoTramiteDto actualizarHistoricoTramite(HistoricoTramiteDto dto) {
        Persona persona = personaRepository.getReferenceById(dto.getSolicitante().getId());
        HistoricoTramite historicoTramite = tramiteMapper.toEntity(dto);
        historicoTramite.setSolicitante(persona);
        historicoTramite = repository.save(historicoTramite);
        return tramiteMapper.toDto(historicoTramite);
    }

    public List<TareaPendiente> listarTareasActivas(String usuario, Pageable pageable, MultiValueMap<String, String> headers) {
        try {
            Page<TareasActivas> list = repository.findAllTareasActivasPage(usuario.toUpperCase(), pageable);
            headers.add("rootSize", list.getTotalElements() + "");
            List<TareasActivas> activas = list.getContent();
            List<TareaPendiente> result = new ArrayList<>(activas.size());
            if (Utils.isNotEmpty(activas)) {
                for (TareasActivas t : list) {
                    TareaPendiente tp = new TareaPendiente();
                    tp.setId(t.getId());
                    tp.setAssignee(t.getAssignee());
                    tp.setCandidate(t.getCandidate());
                    tp.setCreateTime(t.getCreateTime());
                    tp.setIdTramite(t.getIdTramite());
                    tp.setProcInstId(t.getProcInstId());
                    tp.setTaskId(t.getTaskId());
                    tp.setTaskDefKey(t.getTaskDefKey());
                    tp.setName(t.getName());
                    tp.setNumTramite(t.getNumTramite());
                    tp.setFormKey(t.getFormKey());
                    tp.setPriority(t.getPriority());
                    tp.setRev(t.getRev());
                    tp.setNombrePropietario(t.getNombrePropietario());
                    tp.setTipoTramiteId(t.getIdTipoTramite());
                    tp.setReferenciaId(t.getReferenciaId());

                    HistoricoTramite ht = this.historicoTramiteRepo.findById(t.getIdTramite()).orElse(null);
                    HistoricoTramiteDto dto = tramiteMapper.toDto(ht);
                    tp.setTramite(dto);
                    if (ht != null) {
                        tp.setIdTipoTramite(ht.getTipoTramite());
                        if (ht.getSolicitante() != null) {
                            PersonaDto persona = (PersonaDto) restService.restPOST(appProps.getUrlAdministrativo() + "cliente/consultarSolicitante/" + ht.getSolicitante().getId(), null, null, PersonaDto.class);
                            if (persona != null && persona.getNumIdentificacion() != null) {
                                tp.setPersona(persona);
                                if (persona.getTipoIdentificacion() == 1L) {
                                    tp.setNombrePropietario(persona.getApellido() + " " + persona.getNombre());
                                } else {
                                    tp.setNombrePropietario(persona.getNombre());
                                }
                            }
                        }
                        SolicitudServicioDto ws = new SolicitudServicioDto();
                        ws.setId(ht.getReferenciaId());
                        try {
                            SolicitudServicioDto ss = (SolicitudServicioDto) restService.restPOST(appProps.getUrlVentanillaInterna() + "solicitudServicio/listar-id", null, ws, SolicitudServicioDto.class);
                            if (ss != null) {
                                tp.setPredio(ss.getPredio());
                                tp.setSolicitudServicio(ss);
                                dto.setCarpetaRep(ss.getTramite());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    result.add(tp);
                }
            }
            return result;
        } catch (Exception e) {//0939051247
            e.printStackTrace();
        }
        return null;
    }

    public List<TareaPendiente> listarTareasActivasOptimizado(String usuario, Pageable pageable, MultiValueMap<String, String> headers) {
        if (StringUtils.isBlank(usuario)) {
            throw new IllegalArgumentException("Usuario no puede estar vacío");
        }

        try {
            // 1. Optimización consulta principal
            Page<TareasActivas> list = repository.findAllTareasActivasPage(usuario.toUpperCase(), pageable);
            headers.add("rootSize", String.valueOf(list.getTotalElements()));

            if (list.isEmpty()) {
                return Collections.emptyList();
            }

            // 2. Procesamiento en paralelo con streams
            return list.getContent().parallelStream()
                    .map(this::convertirTareaPendiente)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private TareaPendiente convertirTareaPendiente(TareasActivas t) {
        TareaPendiente tp = new TareaPendiente();
        BeanUtils.copyProperties(t, tp);

        HistoricoTramite ht = historicoTramiteRepo.findById(t.getIdTramite()).orElse(null);
        if (ht == null) {
            return tp;
        }

        HistoricoTramiteDto dto = tramiteMapper.toDto(ht);
        tp.setTramite(dto);
        tp.setIdTipoTramite(ht.getTipoTramite());

        completarDatosPersona(tp, ht);
        completarDatosSolicitud(tp, ht);

        return tp;
    }

    public void completarDatosPersona(TareaPendiente tp, HistoricoTramite ht) {
        if (ht.getSolicitante() == null) return;

        try {
            PersonaDto persona = obtenerDatosPersona(ht.getSolicitante().getId());
            if (persona != null && persona.getNumIdentificacion() != null) {
                tp.setPersona(persona);
                String nombreCompleto = persona.getTipoIdentificacion() == 1L
                        ? persona.getApellido() + " " + persona.getNombre()
                        : persona.getNombre();
                tp.setNombrePropietario(nombreCompleto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Cacheable(value = "personaCache", key = "#idSolicitante")
    public PersonaDto obtenerDatosPersona(Long idSolicitante) {
        return (PersonaDto) restService.restPOST(
                appProps.getUrlAdministrativo() + "cliente/consultarSolicitante/" + idSolicitante,
                null,
                null,
                PersonaDto.class
        );
    }

    private void completarDatosSolicitud(TareaPendiente tp, HistoricoTramite ht) {
        if (ht.getReferenciaId() == null) return;

        try {
            SolicitudServicioDto ss = obtenerDatosSolicutid(ht.getReferenciaId());

            if (ss != null) {
                tp.setPredio(ss.getPredio());
                tp.setSolicitudServicio(ss);
                tp.getTramite().setCarpetaRep(ss.getTramite());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Cacheable(value = "personaCache", key = "#referenciaId")
    public SolicitudServicioDto obtenerDatosSolicutid(Long referenciaId) {

        SolicitudServicioDto ws = new SolicitudServicioDto();
        ws.setId(referenciaId);

        return (SolicitudServicioDto) restService.restPOST(
                appProps.getUrlVentanillaInterna() + "solicitudServicio/listar-id",
                null,
                ws,
                SolicitudServicioDto.class
        );
    }

    private boolean esCedulaValida(String usuarioCreacion) {
        return usuarioCreacion != null && usuarioCreacion.matches("\\d{10}");
    }

    private boolean esRucValido(String usuarioCreacion) {
        return usuarioCreacion != null && usuarioCreacion.matches("\\d{13}");
    }
}
