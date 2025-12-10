package org.ibarra.service;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.ibarra.conf.AppProps;
import org.ibarra.dto.*;
import org.ibarra.dto.Process;
import org.ibarra.entity.*;
import org.ibarra.mapper.DocumentoTramiteMapper;
import org.ibarra.mapper.HistoricoTramiteMapper;
import org.ibarra.mapper.TipoTramiteMapper;
import org.ibarra.mapper.TipoTramiteRequisitoMapper;
import org.ibarra.repository.*;
import org.ibarra.util.Constantes;
import org.ibarra.util.Utils;
import org.ibarra.util.ValoresCodigo;
import org.ibarra.util.model.BusquedaDinamica;
import org.ibarra.util.model.EstadoTramite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcessService {
    private Logger Log = LoggerFactory.getLogger(ProcessService.class);
    @Autowired
    private HistoricoTramiteService tramiteService;
    @Autowired
    private RuntimeService service;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private DocumentoTramiteMapper documentoTramiteMapper;
    @Autowired
    private DocumentoTramiteRepo documentoRepo;
    @Autowired
    private TipoTramiteRequisitoRepo tipoTramiteRequisitoRepo;
    @Autowired
    private TipoTramiteRequisitoMapper tipoTramiteRequisitoMapper;
    @Autowired
    private IdentityLinkRepository identityLinkRepository;
    @Autowired
    private HistoricoTramiteRepo repository;
    @Autowired
    private TipoTramiteRepo tipoTramiteRepo;

    @Autowired
    private TipoTramiteMapper tipoTramiteMapper;
    @Autowired
    private HistoricoTramiteMapper historicoTramiteMapper;
    @Autowired
    private HistoricoTramiteObservacionRepo historicoTramiteObservacionRepo;
    @Autowired
    private RestService restService;
    @Autowired
    private AppProps appProps;
    @Autowired
    private EnvioAlertarService envioAlertarService;

    @Autowired
    private EstadoTareaService estadoTareaService;

    public ProcessService() {

    }

    public File getFileBpmn(String fileBpmn) {
        try {
            String ruta = appProps.getPathBpmn() + fileBpmn;
            ruta = ruta.replace("//", "/");
            ruta = ruta.replace("procesos/procesos", "procesos");
            System.out.println("Ruta del archivo bpmn: " + ruta);
            File file = ResourceUtils.getFile(ruta);
            return file;
        } catch (FileNotFoundException e) {
            Log.error(e.getMessage());
        }
        return null;
    }

    public RespuestaWs processImplement(TipoTramiteDto data) {
        try {
            if (!data.getArchivoBpmn().isEmpty()) {
                File bpmn = getFileBpmn(data.getArchivoBpmn());
                if (bpmn != null) {
                    System.out.println("Archivo: " + bpmn);
                    Deployment deploy = repositoryService.createDeployment().addInputStream(data.getArchivoBpmn(), new FileInputStream(bpmn)).deploy();
                    if (deploy != null) {
                        Log.info(" Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count() + " " + deploy.getId() + " " + deploy.getName());
                    }

                    return new RespuestaWs(Boolean.TRUE, Utils.gsonTransform(data), Constantes.procesoCorrecto);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
    }


    public RespuestaWs processStart(Process data) {
        try {
            if (data.getTipoTramite() != null && Utils.isNotEmptyString(data.getTipoTramite().getAbreviatura())) {
                System.out.println("Llega: " + data.getTipoTramite().getAbreviatura());
                TipoTramite tipoTramite = tipoTramiteRepo.findByAbreviaturaAndEstado(data.getTipoTramite().getAbreviatura(), Boolean.TRUE);
                data.setTipoTramite(tipoTramiteMapper.toDto(tipoTramite));
            } else if (data.getTipoTramite() != null && data.getTipoTramite().getId() != null) {
                data.setTipoTramite(tipoTramiteMapper.toDto(tipoTramiteRepo.findById(data.getTipoTramite().getId()).get()));
            }

            if (data.getTipoTramite() == null || data.getTipoTramite().getId() == null) {
                System.out.println("No existe tramite con abreviatura " + data.getTipoTramite().getAbreviatura() + " con los datos " + data);
                return new RespuestaWs(Boolean.FALSE, null, Constantes.ingreseTipoTramite);
            }
            System.out.println("getTipoTramite() " + data.getTipoTramite().getAbreviatura());
            /*if (data.getTipoTramite().getAbreviatura() == null) {
                return new RespuestaWs(Boolean.FALSE, null, Constantes.faltaAbrTipoTramite);
            }*/
            this.service = processEngine.getRuntimeService();
            HistoricoTramite historicoTramite = tramiteService.crearTramite(data);


            if (historicoTramite != null) {
                System.out.println("parametros del trámite:" + data.getParametros());
                data.getParametros().put("tramite", historicoTramite.getTramite() + " activitiKey " + data.getTipoTramite().getActivitykey());
                ProcessInstance processInstance = this.service.startProcessInstanceByKey(data.getTipoTramite().getActivitykey(), data.getParametros());
                Log.info("Number of process instances: " + this.service.createProcessInstanceQuery().count() + " ProcessDefinitionId " + processInstance.getProcessDefinitionId() + " DeploymentId " + processInstance.getDeploymentId());
                Task instance = taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByTaskPriority().desc().orderByTaskCreateTime().asc().active().singleResult();
                Tarea tarea = taskToDto(instance);
                tarea.setObservacionUsuario(data.getUsuarioObservacion());
                tarea.setObservacion(data.getObservacion());
                tramiteService.actualizarTramite(historicoTramite.getId(), processInstance, tarea);
                HistoricoTramiteDto dto = tramiteService.consultarHistoricoTramite(processInstance.getProcessInstanceId());
                tarea.setTramite(dto);
                if (Constantes.inicioTramite.equals(tarea.getObservacion())) {
                    tarea.setObservacion("");
                }
                return new RespuestaWs(Boolean.TRUE, Utils.gsonTransform(tarea), Constantes.procesoCorrecto);
            }
        } catch (Exception e) {
            Log.error("ERROR PROCESS INSTANCE " + data.getTipoTramite().getActivitykey());
            e.printStackTrace();
        }
        return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
    }

    public RespuestaWs guardarDocumentos(DocumentoTramiteDto data) {
        DocumentoTramite documento = documentoTramiteMapper.toEntity(data);
        documento = documentoRepo.save(documento);
        return new RespuestaWs(Boolean.TRUE, Utils.gsonTransform(documento), Constantes.datosCorrecto);

    }

    public RespuestaWs eliminarDocumentos(DocumentoTramiteDto doc) {
        try {
            DocumentoTramite data = documentoTramiteMapper.toEntity(doc);
//            if (data.getRequisito() != null) {
//                data.setReferenciaDoc(null);
//                documentoRepo.save(data);
//            } else {
            documentoRepo.delete(data);
//            }
            return new RespuestaWs(Boolean.TRUE, null, Constantes.datosCorrecto);

        } catch (Exception e) {
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);

        }
    }

    public List<DocumentoTramiteDto> listarDocumentos(DocumentoTramiteDto dto) {
        try {
            if (dto.getClaseName() != null && dto.getIdReferencia() != null) {
                return documentoRepo.findAllByClaseNameAndIdReferencia(dto.getClaseName(), dto.getIdReferencia()).stream().map(data -> documentoTramiteMapper.toDto(data)).collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public DocumentoTramiteDto buscarDocumento(Long code) {
        if (code != null) {
            DocumentoTramite doc = documentoRepo.getReferenceById(code);
            return documentoTramiteMapper.toDto(doc);
        }
        return new DocumentoTramiteDto();
    }

    public List<TipoTramiteRequisitoDto> listarRequisitos(TipoTramiteDto dto) {
        List<TipoTramiteRequisito> tmp = tipoTramiteRequisitoRepo.findAllByTipoTramite_IdAndEstado(dto.getId(), ValoresCodigo.estadoActivo);
        if (tmp != null && tmp.size() > 0) {
            return tmp.stream().map(data -> tipoTramiteRequisitoMapper.toDto(data)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<TipoTramiteRequisitoDto> listarSubequisitos(Long dto) {
        List<TipoTramiteRequisito> tmp = tipoTramiteRequisitoRepo.findAllByPadre_Id(dto);
        if (tmp != null && tmp.size() > 0) {
            return tmp.stream().map(data -> tipoTramiteRequisitoMapper.toDto(data)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public String taskComplete(Tarea tarea) {
        this.taskService = this.processEngine.getTaskService();
        try {
            taskService.complete(tarea.getTaskId(), tarea.getParametros());
            System.out.println("Tarea Completada " + tarea.getTaskId() + " tarea.getParametros() " + tarea.getParametros());
            tramiteService.guardarObservacion(tarea.getTramite().getId(), tarea.getObservacionUsuario(), tarea);
            System.out.println("Tarea Observaciones " + tarea.getTaskId() + " tarea.getNotificacionAsignacion(): " + tarea.getNotificacionAsignacion());
            if (tarea.getNotificacionAsignacion() != null && Boolean.TRUE.equals(tarea.getNotificacionAsignacion())) {
                String idProceso = tarea.getTramite().getIdProceso();
                if (idProceso != null) {
                    Task task = this.processEngine.getTaskService().createTaskQuery().active().processInstanceId(idProceso).singleResult();
                    System.out.println("// enviando notificaicon de asignacion de tarea");
                    envioAlertarService.enviarCorreoTareaAsignada(task, tarea.getTramite());
                }
            }
            return "COMPLETE TASK SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "COMPLETE TASK FAILURE";
        }
    }

    public InputStream processDiagram(String processId, String nameDiagram) {
        // this.processEngine = ProcessEngines.getDefaultProcessEngine();
        return this.processEngine.getRepositoryService().getResourceAsStream(processId, nameDiagram);
    }

    public List<Task> lisTaskActiveProcessInstanceId(String taskID) {
        // this.processEngine = ProcessEngines.getDefaultProcessEngine();
        this.taskService = processEngine.getTaskService();
        List<Task> taskAssigne;
        try {
            taskAssigne = taskService.createTaskQuery().processInstanceId(taskID).orderByTaskPriority().desc().orderByTaskCreateTime().asc().list();
            Log.info("Total de Tareas %s", taskAssigne.size());
            return taskAssigne;
        } catch (Exception e) {
            Log.error("ERROR En ");
            return null;

        }
    }

    public List<Tarea> lisTaskProcessInstanceId(String process) {
        // this.processEngine = ProcessEngines.getDefaultProcessEngine();
        this.taskService = processEngine.getTaskService();
        List<HistoricTaskInstance> allTask = new ArrayList<>();
        List<Tarea> resultado = new ArrayList<>();
        try {


            allTask = this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(process).orderByHistoricTaskInstanceEndTime().desc().orderByTaskCreateTime().desc().list();
            List<HistoricProcessInstance> lista = this.processEngine.getHistoryService().createHistoricProcessInstanceQuery().superProcessInstanceId(process).list();
            if (lista != null) {
                if (allTask == null) {
                    allTask = new ArrayList();
                }
                for (HistoricProcessInstance hpi : lista) {
                    List<HistoricTaskInstance> subList = this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(hpi.getId()).orderByHistoricTaskInstanceEndTime().desc().orderByTaskCreateTime().desc().list();
                    allTask.addAll(subList);
                }
            }

            if (allTask != null && allTask.size() > 0) {
                for (HistoricTaskInstance instance : allTask) {
                    Tarea data = new Tarea();
                    data.setFormKey(instance.getFormKey());
                    data.setTaskId(instance.getId());
                    data.setTaskDefinitionKey(instance.getTaskDefinitionKey());
                    data.setTaskName(instance.getName());
                    data.setAssignee(instance.getAssignee());
                    data.setPriority(instance.getPriority());
                    data.setProcessInstanceID(instance.getProcessInstanceId());
                    data.setFechaInicio(instance.getStartTime());
                    data.setFechaFin(instance.getEndTime());
                    data.setEstado(estadoTareaService.obtenerEstado(instance));
                    data.setTramite(tramiteService.consultarHistoricoTramite(instance.getProcessInstanceId()));
                    resultado.add(data);
                }
            }

            return resultado;
        } catch (Exception e) {
            Log.error("ERROR En ");
            return null;

        }
    }

    public Tarea consultarXtramite(String tramite, String usuario) {
        try {
            HistoricoTramiteDto historicoTramite = tramiteService.consultarXtramite(tramite);
            System.out.println("historicoTramite " + historicoTramite.getTramite());
            List<Task> tasks = lisTaskActiveProcessInstanceId(historicoTramite.getIdProceso());
            if (tasks == null) {
                tasks = new ArrayList<>();
            }

            Log.info("tasks {}", tasks.size());
            Task instance = null;
            if (Utils.isEmptyString(usuario)) {
                instance = tasks.get(0);
            } else {
                for (Task t : tasks) {
                    if (usuario.equals(t.getAssignee())) {
                        instance = t;
                        break;
                    }
                }
                if (instance == null) {
                    instance = tasks.get(0);
                }
            }

            Tarea data = taskToDto(instance);
            Log.info("{}", instance);
            return data;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Tarea consultarHistoricoXtramite(String tramite) {
        try {
            HistoricoTramiteDto historicoTramite = tramiteService.consultarXtramite(tramite);
            List<Tarea> tasks = listTaskMap(historicoTramite.getIdProceso());
            if (tasks == null) {
                tasks = new ArrayList<>();
            }
            Log.info("tasks {}", tasks.size());
            //Task instance = tasks.get(0);
            Tarea data = tasks.get(0);// taskToDto(instance);
            if (data.getTramite() != null)
                data.setObservaciones(tramiteService.observaciones(data.getTramite().getId()));
            Log.info("{}", data);
            return data;

        } catch (Exception ex) {
            return null;
        }
    }

    public Tarea taskActiveXtramite(Tarea tramite) {

        Tarea tarea = consultarXtramite(tramite.getTramite().getTramite(), tramite.getAssignee());
        tarea = taskActive(tarea.getProcessInstanceID(), tramite.getAssignee());

        return tarea;

    }

    public Tarea taskActive(String taskID, String usuario) {
        try {
            List<Task> tasks = lisTaskActiveProcessInstanceId(taskID);
            if (tasks == null) {
                tasks = new ArrayList<>();
            }
            Task instance = null;
            if (Utils.isEmptyString(usuario)) {
                instance = tasks.get(0);
            } else {
                for (Task t : tasks) {
                    if (usuario.equals(t.getAssignee())) {
                        instance = t;
                        break;
                    }
                }
                if (instance == null) {
                    instance = tasks.get(0);
                }
            }
            System.out.println("instance.getId() " + instance.getId());
            Map<String, Object> taskVariables = taskService.getVariables(instance.getId());

            Tarea tarea = taskToDto(instance);
            tarea.setParametrosTarea(taskVariables);

            if (tarea.getTaskDefinitionKey().startsWith("multiTask")) {
                tarea.setMultiTask(Boolean.TRUE);

                int totalInstances = (Integer) tarea.getParametrosTarea().get("nrOfInstances");
                int completedInstances = (Integer) tarea.getParametrosTarea().get("nrOfCompletedInstances");
                int remainingInstances = (Integer) tarea.getParametrosTarea().get("nrOfActiveInstances");

                tarea.setInstanciasCompletadas(completedInstances);
                tarea.setInstanciasRestantes(remainingInstances);
                tarea.setTotalInstancias(totalInstances);

            } else {
                tarea.setMultiTask(Boolean.FALSE);
            }
            return tarea;

        } catch (Exception ex) {
            return null;
        }
    }

    private Tarea taskToDto(Task instance) {
        Tarea data = new Tarea();
        if (instance != null) {
            data.setFormKey(instance.getFormKey());
            data.setTaskId(instance.getId());
            data.setTaskDefinitionKey(instance.getTaskDefinitionKey());
            data.setTaskName(instance.getName());
            data.setAssignee(instance.getAssignee());
            data.setPriority(instance.getPriority());
            data.setProcessInstanceID(instance.getProcessInstanceId());
            data.setTramite(tramiteService.consultarHistoricoTramite(instance.getProcessInstanceId()));
            data.setEstado(estadoTareaService.obtenerEstado(instance));
        }

        if (data.getTramite() != null) data.setObservaciones(tramiteService.observaciones(data.getTramite().getId()));
        return data;
    }

    public void reassignTask(String taskId, String user) {
        // this.processEngine = ProcessEngines.getDefaultProcessEngine();
        this.taskService = processEngine.getTaskService();
        this.taskService.setAssignee(taskId, user);
    }

    public RespuestaWs deleteInstanceProcess(String processId, String observaciones) {
        try {
            // this.processEngine = ProcessEngines.getDefaultProcessEngine();
            this.service = processEngine.getRuntimeService();
            this.service.deleteProcessInstance(processId, observaciones);

            return new RespuestaWs(Boolean.TRUE, null, Constantes.datosCorrecto);
        } catch (Exception e) {
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
        }
    }


    public InputStream diagramEspeciality(String procInstId) {
//        // this.processEngine = ProcessEngines.getDefaultProcessEngine();
//        ProcessInstance instance = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
//
//        if (instance != null) {
//            BpmnModel bpmnModel = this.processEngine.getRepositoryService().getBpmnModel(instance.getProcessDefinitionId());
//            List<String> activeActivityIds = this.processEngine.getRuntimeService().getActiveActivityIds(procInstId);
//            ProcessDiagramGenerator diagramGenerator = this.processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
//            return diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
//        } else {
//            HistoricProcessInstance historicInstance = this.processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
//            BpmnModel bpmnModel = this.processEngine.getRepositoryService().getBpmnModel(historicInstance.getProcessDefinitionId());
//            List<String> activeActivityIds = new ArrayList<String>();
//            List<HistoricActivityInstance> list = this.processEngine.getHistoryService().createNativeHistoricActivityInstanceQuery()
//                    .sql("SELECT * FROM " + this.processEngine.getManagementService().getTableName(HistoricActivityInstance.class) + " x WHERE x.proc_inst_id_= #{procInstId} ")
//                    .parameter("procInstId", procInstId).list();
//            if (list != null) {
//                list.forEach((hai) -> {
//                    activeActivityIds.add(hai.getActivityId());
//                });
//            }
//            ProcessDiagramGenerator diagramGenerator = this.processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
//            return diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
//        }
        return null;
    }


    /*Consulta los tramites de todos los usuarios y por todos los estados*/
    public Map<String, Object> consultarTareasTodos(int pagina, int row, Tramites tramite) {
        try {
            Map<String, Object> res;
            res = consultarTareas(pagina, row, "", tramite);
            return res;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HashMap<>();
        }

    }

    public Map<String, Object> consultarTareaTodasVentanillaPublica(int pagina, int row, Tramites tramite) {
        try {
            Map<String, Object> res;
            res = consultarTareasVentanillaPublica(pagina, row, "", tramite);
            return res;
        } catch (Exception ex) {
//            ex.printStackTrace(System.out);
            ex.printStackTrace(System.out);
            return new HashMap<>();
        }

    }

    public Map<String, Object> consultarTareaTodasAtencionCiudadana(int pagina, int row, Tramites tramite) {
        try {
            Map<String, Object> res;
            res = consultarTareasAtencionCiudadana(pagina, row, "", tramite);
            return res;
        } catch (Exception ex) {
//            ex.printStackTrace(System.out);
            ex.printStackTrace(System.out);
            return new HashMap<>();
        }

    }


    /*Consulta los tramites segun el usuario actual y por todos los estados*/
    public Map<String, Object> consultaTramitesUsuario(int pagina, int row, String usuario, Tramites tramite) {
        try {
            Map<String, Object> res;
            res = consultarTareas(pagina, row, usuario, tramite);
            return res;
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            return new HashMap<>();
        }

    }

    /* Consulta las tareas por todos los estados finalizadas,en proceso,pendientes,etc por usuario
     */
    @Cacheable(value = "tareaCache", key = "#usuario + #pagina + #row")
    public Map<String, Object> consultarTareas(int pagina, int row, String usuario, Tramites tramite) {
        try {
            this.taskService = processEngine.getTaskService();
            List<Tramites> result = new ArrayList<>();
            List<String> processInstans = null;
            Boolean filtrar = false;
            if (tramite != null) {
                if (tramite.getTramite() != null) {
                    HistoricoTramite dHistoricoTramite = this.historicoTramiteMapper.toEntity(tramite.getTramite());
                    List<HistoricoTramite> all = this.repository.findAll(Example.of(dHistoricoTramite, ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)));
                    System.out.println(dHistoricoTramite);
                    if (Utils.isNotEmpty(all)) {
                        filtrar = true;
                        processInstans = all.stream().map(HistoricoTramite::getIdProceso).filter(Objects::nonNull).collect(Collectors.toList());
                    }
                }
            }
            List<HistoricTaskInstance> taskHistory = new ArrayList<>();
            HistoricTaskInstanceQuery instanceQuery = null;
            if (filtrar) {
                if (usuario.equals("")) {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceIdIn(processInstans).orderByTaskCreateTime().desc();

                } else {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().orderByTaskCreateTime().taskAssignee(usuario).processInstanceIdIn(processInstans).desc();
                }
            } else {
                if (usuario.equals("")) {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().orderByTaskCreateTime().desc();

                } else {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().orderByTaskCreateTime().taskAssignee(usuario).desc();
                }
            }

            taskHistory = instanceQuery.listPage(pagina, row);
            Log.info("taskHistory " + taskHistory.size());
            if (taskHistory != null && taskHistory.size() > 0) {
                for (HistoricTaskInstance instance : taskHistory) {
                    Tramites data = new Tramites();
                    if (instance.getProcessDefinitionId() != null) {
                        data.setProcDefId(instance.getProcessDefinitionId());
                    }
                    if (instance.getProcessInstanceId() != null) {
                        data.setProcInstId(instance.getProcessInstanceId());
                    }
                    if (instance.getDurationInMillis() != null) {
                        data.setDuration(instance.getDurationInMillis().toString());

                    }
                    if (instance.getDeleteReason() != null) {
                        data.setDeleteReason(instance.getDeleteReason());
                    }
                    if (instance.getEndTime() != null) {
                        data.setEndTime(instance.getEndTime());
                    }
                    if (instance.getStartTime() != null) {
                        data.setStartTime(instance.getStartTime());
                    }
                    if (instance.getProcessInstanceId() != null) {
                        data.setTramite(tramiteService.consultarHistoricoTramite(instance.getProcessInstanceId()));
                    }
                    // Validar estado del tramite
                    data.setEstado(getEstadoTramite(instance));
                    result.add(data);
                }

                List<String> aniadidos = new ArrayList<>();
                List<Tramites> formateados = new ArrayList<>();
                final long[] count = {instanceQuery.count()};
//                result.forEach(data -> {
//                    if (aniadidos != null && aniadidos.size() > 0) {
//                        if (!aniadidos.contains(data.getProcInstId())) {
//                            formateados.add(data);
//                            aniadidos.add(data.getProcInstId());
//                        } else {
//                            count[0] = count[0] - 1;
//                        }
//                    } else {
//                        formateados.add(data);
//                        aniadidos.add(data.getProcInstId());
//                    }
//                });
                result.parallelStream().filter(data -> !aniadidos.contains(data.getProcInstId())).forEach(data -> {
                    formateados.add(data);
                    aniadidos.add(data.getProcInstId());
                });


                PagedListHolder<Object> page = new PagedListHolder(formateados);
                page.setPageSize(row); // number of items per page
                page.setPage(pagina);      // set to first page
                System.out.println("page list size:" + page.getPageList().size());
                List<Object> view = page.getPageList();
                Map<String, Object> res = new HashMap<>();
                res.put("result", view);
                res.put("totalPage", page.getPageCount());
                res.put("rootSize", count[0]);
                return res;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("totalPage", 0);
            map.put("rootSize", 0);
            return map;
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            HashMap<String, Object> map = new HashMap<>();
            map.put("totalPage", 0);
            map.put("rootSize", 0);
            return map;
        }

    }

    public Map<String, Object> consultarTareasVentanillaPublica(int pagina, int row, String usuario, Tramites tramite) {
        try {
            // this.processEngine = ProcessEngines.getDefaultProcessEngine();
            this.taskService = processEngine.getTaskService();
            List<Tramites> result = new ArrayList<>();
            List<String> processInstans = null;
            List<String> processInstansAfuera = null;
            Boolean filtrar = false;
            if (tramite != null) {
                if (tramite.getTramite() != null) {
                    HistoricoTramite dHistoricoTramite = this.historicoTramiteMapper.toEntity(tramite.getTramite());
                    List<HistoricoTramite> all = this.repository.findAll(Example.of(dHistoricoTramite, ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)));
                    System.out.println(dHistoricoTramite);
                    if (Utils.isNotEmpty(all)) {
                        filtrar = true;
                        processInstans = all.stream().map(HistoricoTramite::getIdProceso).filter(Objects::nonNull).collect(Collectors.toList());

                    }
                }
            }
            HistoricoTramite dHistoricoTramiteAfuera = this.historicoTramiteMapper.toEntity(tramite.getTramite());
            List<HistoricoTramite> allAfuera = this.repository.findByTipoTramiteVentanillaPublica(Boolean.TRUE);
            System.out.println(dHistoricoTramiteAfuera);
            if (Utils.isNotEmpty(allAfuera)) {
                processInstansAfuera = allAfuera.stream().map(HistoricoTramite::getIdProceso).filter(Objects::nonNull).collect(Collectors.toList());
            }
            List<HistoricTaskInstance> taskHistory = new ArrayList<>();
            HistoricTaskInstanceQuery instanceQuery = null;
            if (filtrar) {
                if (usuario.equals("")) {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceIdIn(processInstans).orderByTaskCreateTime().desc();

                } else {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().orderByTaskCreateTime().taskAssignee(usuario).processInstanceIdIn(processInstans).desc();
                }
            } else {
                if (usuario.equals("")) {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().orderByTaskCreateTime().processInstanceIdIn(processInstansAfuera).desc();

                } else {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().orderByTaskCreateTime().taskAssignee(usuario).processInstanceIdIn(processInstansAfuera).desc();
                }
            }

            taskHistory = instanceQuery.listPage(pagina, row);
            Log.info("taskHistory " + taskHistory.size());
            if (taskHistory != null && taskHistory.size() > 0) {
                for (HistoricTaskInstance instance : taskHistory) {
                    Tramites data = new Tramites();
                    if (instance.getProcessDefinitionId() != null) {
                        data.setProcDefId(instance.getProcessDefinitionId());
                    }
                    if (instance.getProcessInstanceId() != null) {
                        data.setProcInstId(instance.getProcessInstanceId());
                    }
                    if (instance.getDurationInMillis() != null) {
                        data.setDuration(instance.getDurationInMillis().toString());

                    }
                    if (instance.getDeleteReason() != null) {
                        data.setDeleteReason(instance.getDeleteReason());
                    }
                    if (instance.getEndTime() != null) {
                        data.setEndTime(instance.getEndTime());
                    }
                    if (instance.getStartTime() != null) {
                        data.setStartTime(instance.getStartTime());
                    }
                    if (instance.getProcessInstanceId() != null) {
                        data.setTramite(tramiteService.consultarHistoricoTramite(instance.getProcessInstanceId()));
                    }
                    // Validar estado del tramite
                    data.setEstado(getEstadoTramite(instance));
                    result.add(data);
                }

                List<String> aniadidos = new ArrayList<>();
                List<Tramites> formateados = new ArrayList<>();
                final long[] count = {instanceQuery.count()};
                result.forEach(data -> {
                    if (aniadidos != null && aniadidos.size() > 0) {
                        if (!aniadidos.contains(data.getProcInstId())) {
                            formateados.add(data);
                            aniadidos.add(data.getProcInstId());
                        } else {
                            count[0] = count[0] - 1;
                        }
                    } else {
                        formateados.add(data);
                        aniadidos.add(data.getProcInstId());
                    }
                });


                PagedListHolder<Object> page = new PagedListHolder(formateados);
                page.setPageSize(row); // number of items per page
                page.setPage(pagina);      // set to first page
                System.out.println("page list size:" + page.getPageList().size());
                List<Object> view = page.getPageList();
                Map<String, Object> res = new HashMap<>();
                res.put("result", view);
                res.put("totalPage", page.getPageCount());
                res.put("rootSize", count[0]);
                return res;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("totalPage", 0);
            map.put("rootSize", 0);
            return map;
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            HashMap<String, Object> map = new HashMap<>();
            map.put("totalPage", 0);
            map.put("rootSize", 0);
            return map;
        }
    }

    public Map<String, Object> consultarTareasAtencionCiudadana(int pagina, int row, String usuario, Tramites tramite) {
        try {
            // this.processEngine = ProcessEngines.getDefaultProcessEngine();
            this.taskService = processEngine.getTaskService();
            List<Tramites> result = new ArrayList<>();
            List<String> processInstans = null;
            List<String> processInstansAfuera = null;
            Boolean filtrar = false;
            if (tramite != null) {
                if (tramite.getTramite() != null) {
                    HistoricoTramite dHistoricoTramite = this.historicoTramiteMapper.toEntity(tramite.getTramite());
                    List<HistoricoTramite> all = this.repository.findAll(Example.of(dHistoricoTramite, ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)));
                    System.out.println(dHistoricoTramite);
                    if (Utils.isNotEmpty(all)) {
                        filtrar = true;
                        processInstans = all.stream().map(HistoricoTramite::getIdProceso).filter(Objects::nonNull).collect(Collectors.toList());

                    }
                }
            }
            HistoricoTramite dHistoricoTramiteAfuera = this.historicoTramiteMapper.toEntity(tramite.getTramite());
            List<HistoricoTramite> allAfuera = this.repository.findByTipoTramite(tipoTramiteRepo.findByAbreviaturaAndEstado("CIU", Boolean.TRUE));
            System.out.println(dHistoricoTramiteAfuera);
            if (Utils.isNotEmpty(allAfuera)) {
                processInstansAfuera = allAfuera.stream().map(HistoricoTramite::getIdProceso).filter(Objects::nonNull).collect(Collectors.toList());
            }
            List<HistoricTaskInstance> taskHistory = new ArrayList<>();
            HistoricTaskInstanceQuery instanceQuery = null;
            if (filtrar) {
                if (usuario.equals("")) {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceIdIn(processInstans).orderByTaskCreateTime().desc();

                } else {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().orderByTaskCreateTime().taskAssignee(usuario).processInstanceIdIn(processInstans).desc();
                }
            } else {
                if (usuario.equals("")) {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().orderByTaskCreateTime().processInstanceIdIn(processInstansAfuera).desc();

                } else {
                    instanceQuery = processEngine.getHistoryService().createHistoricTaskInstanceQuery().orderByTaskCreateTime().taskAssignee(usuario).processInstanceIdIn(processInstansAfuera).desc();
                }
            }

            taskHistory = instanceQuery.listPage(pagina, row);
            Log.info("taskHistory " + taskHistory.size());
            if (taskHistory != null && taskHistory.size() > 0) {
                for (HistoricTaskInstance instance : taskHistory) {
                    Tramites data = new Tramites();
                    if (instance.getProcessDefinitionId() != null) {
                        data.setProcDefId(instance.getProcessDefinitionId());
                    }
                    if (instance.getProcessInstanceId() != null) {
                        data.setProcInstId(instance.getProcessInstanceId());
                    }
                    if (instance.getDurationInMillis() != null) {
                        data.setDuration(instance.getDurationInMillis().toString());

                    }
                    if (instance.getDeleteReason() != null) {
                        data.setDeleteReason(instance.getDeleteReason());
                    }
                    if (instance.getEndTime() != null) {
                        data.setEndTime(instance.getEndTime());
                    }
                    if (instance.getStartTime() != null) {
                        data.setStartTime(instance.getStartTime());
                    }
                    if (instance.getProcessInstanceId() != null) {
                        data.setTramite(tramiteService.consultarHistoricoTramite(instance.getProcessInstanceId()));
                    }
                    // Validar estado del tramite
                    data.setEstado(getEstadoTramite(instance));
                    result.add(data);
                }

                List<String> aniadidos = new ArrayList<>();
                List<Tramites> formateados = new ArrayList<>();
                final long[] count = {instanceQuery.count()};
                result.forEach(data -> {
                    if (aniadidos != null && aniadidos.size() > 0) {
                        if (!aniadidos.contains(data.getProcInstId())) {
                            formateados.add(data);
                            aniadidos.add(data.getProcInstId());
                        } else {
                            count[0] = count[0] - 1;
                        }
                    } else {
                        formateados.add(data);
                        aniadidos.add(data.getProcInstId());
                    }
                });


                PagedListHolder<Object> page = new PagedListHolder(formateados);
                page.setPageSize(row); // number of items per page
                page.setPage(pagina);      // set to first page
                System.out.println("page list size:" + page.getPageList().size());
                List<Object> view = page.getPageList();
                Map<String, Object> res = new HashMap<>();
                res.put("result", view);
                res.put("totalPage", page.getPageCount());
                res.put("rootSize", count[0]);
                return res;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("totalPage", 0);
            map.put("rootSize", 0);
            return map;
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            HashMap<String, Object> map = new HashMap<>();
            map.put("totalPage", 0);
            map.put("rootSize", 0);
            return map;
        }
    }

    public Map<String, Object> consultarTareasTipoTramiteAnio(Tramites tramite, String tipoTramite, Integer periodo, int page, int size) {
        Map<String, Object> res = new HashMap<>();

        Pageable pageable = PageRequest.of(page, size);
        List<HistoricoTramite> historicoTramites = new ArrayList<>();
        Page<HistoricoTramite> paginado = null;
        if (tramite != null && tramite.getTramite() != null && tramite.getTramite().getTramite() != null) {
            System.out.println("1 condicion");
            HistoricoTramite ht = repository.findFirstByTramiteOrderByIdDesc(tramite.getTramite().getTramite());
            if (ht != null) {
                historicoTramites.add(ht);
                res.put("totalPage", 0);
                res.put("rootSize", 1);
            }
        } else if (tipoTramite != null && !tipoTramite.equals("TODOS")) {
            System.out.println("2 condicion " + tipoTramite);

            paginado = repository.findAllByTipoTramite_AbreviaturaAndPeriodoOrderByIdDesc(tipoTramite, periodo, pageable);
            if (!paginado.isEmpty()) {
                historicoTramites = paginado.getContent();

            }
            res.put("totalPage", paginado.getTotalPages());
            res.put("rootSize", paginado.getTotalElements());

        } else {
            System.out.println("3 condicion");

            paginado = repository.findAllByPeriodoOrderByIdDesc(periodo, pageable);
            if (!paginado.isEmpty()) {
                historicoTramites = paginado.getContent();
            }
            res.put("totalPage", paginado.getTotalPages());
            res.put("rootSize", paginado.getTotalElements());
        }

        System.out.println("paginado {}" + paginado);
        List<Tramites> result = new ArrayList<>();
        // this.processEngine = ProcessEngines.getDefaultProcessEngine();
        this.taskService = processEngine.getTaskService();

        if (Utils.isNotEmpty(historicoTramites)) {
            for (HistoricoTramite item : historicoTramites) {
                List<HistoricTaskInstance> latestInstance = processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(item.getIdProceso()).orderByHistoricTaskInstanceEndTime().desc().list();
                HistoricTaskInstance instance = null;
                if (Utils.isNotEmpty(latestInstance)) {
                    instance = latestInstance.get(0);
                }

                if (instance != null) {
                    Tramites data = new Tramites();
                    data.setTramite(tramiteService.consultarHistoricoTramite(item));

                    if (instance.getProcessDefinitionId() != null) {
                        data.setProcDefId(instance.getProcessDefinitionId());
                    }
                    if (instance.getProcessInstanceId() != null) {
                        data.setProcInstId(instance.getProcessInstanceId());
                    }
                    if (instance.getDurationInMillis() != null) {
                        data.setDuration(instance.getDurationInMillis().toString());

                    }
                    if (instance.getDeleteReason() != null) {
                        data.setDeleteReason(instance.getDeleteReason());
                    }
                    if (instance.getEndTime() != null) {
                        data.setEndTime(instance.getEndTime());
                    }
                    if (instance.getStartTime() != null) {
                        data.setStartTime(instance.getStartTime());
                    }


                    result.add(data);
                }
            }

        }

        res.put("result", result);


        return res;
    }


    public List<ProcessDeploy> listProcessDeploy() {
        List<ProcessDeploy> result = new ArrayList<>();
        // this.processEngine = ProcessEngines.getDefaultProcessEngine();
        List<ProcessDefinition> items = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().list();
        for (ProcessDefinition datos : items) {
            ProcessDeploy d = new ProcessDeploy();
            d.setDeploymentId(datos.getDeploymentId());
            d.setCategory(datos.getCategory());
            d.setEngineVersion(datos.getEngineVersion());
            d.setTenantId(datos.getTenantId());
            d.setDescription(datos.getDescription());
            d.setId(datos.getId());
            d.setKey(datos.getKey());
            d.setResourceName(datos.getResourceName());
            d.setDiagramResoruceName(datos.getDiagramResourceName());
            d.setSuspende(datos.isSuspended());
            d.setName(datos.getName());
            d.setVersion(datos.getVersion());
            result.add(d);
        }
        return result;
    }


    public Map<String, Object> listProcessHistory(int paging, int size) {
        List<ProcessDefinition> list = new ArrayList<>();
        List<ProcessDeploy> data = new ArrayList<>();
        // this.processEngine = ProcessEngines.getDefaultProcessEngine();
        list = this.processEngine.getRepositoryService().createProcessDefinitionQuery().orderByProcessDefinitionKey().asc().list();
        for (ProcessDefinition items : list) {
            ProcessDeploy d = new ProcessDeploy();
            d.setDeploymentId(items.getDeploymentId());
            d.setCategory(items.getCategory());
            d.setEngineVersion(items.getEngineVersion());
            d.setTenantId(items.getTenantId());
            d.setDescription(items.getDescription());
            d.setId(items.getId());
            d.setKey(items.getKey());
            d.setResourceName(items.getResourceName());
            d.setDiagramResoruceName(items.getDiagramResourceName());
            d.setSuspende(items.isSuspended());
            d.setName(items.getName());
            d.setVersion(items.getVersion());
            data.add(d);
        }
        PagedListHolder<Object> page = new PagedListHolder(data);
        page.setPageSize(size); // number of items per page
        page.setPage(paging);      // set to first page
        List<Object> view = page.getPageList();  // a List which represents the current page which is the sublist
        Map<String, Object> res = new HashMap<>();
        res.put("result", view);
        res.put("totalPage", page.getPageCount());
        res.put("rootSize", page.getPageSize());
        return res;
    }


    public List<ProcessDeploy> listProcessHistory() {
        List<ProcessDefinition> list = new ArrayList<>();
        List<ProcessDeploy> data = new ArrayList<>();
        // this.processEngine = ProcessEngines.getDefaultProcessEngine();
        list = this.processEngine.getRepositoryService().createProcessDefinitionQuery().orderByProcessDefinitionVersion().asc().list();
        for (ProcessDefinition items : list) {
            ProcessDeploy d = new ProcessDeploy();
            d.setDeploymentId(items.getDeploymentId());
            d.setCategory(items.getCategory());
            d.setEngineVersion(items.getEngineVersion());
            d.setTenantId(items.getTenantId());
            d.setDescription(items.getDescription());
            d.setId(items.getId());
            d.setKey(items.getKey());
            d.setResourceName(items.getResourceName());
            d.setDiagramResoruceName(items.getDiagramResourceName());
            d.setSuspende(items.isSuspended());
            d.setName(items.getName());
            d.setVersion(items.getVersion());
            data.add(d);
        }
        return data;
    }


    public Object getVaribaleInstanceProcess(String processInstanceId, String varName) {
        // this.processEngine = ProcessEngines.getDefaultProcessEngine();
        this.service = processEngine.getRuntimeService();
        System.out.println(this.service.getVariableInstances(processInstanceId));

        Object resultad = this.service.getVariable(processInstanceId, varName);
        System.out.println("res " + resultad);
        if (resultad != null) {
            System.out.println("res " + resultad);
            return resultad;
        } else {
            return null;
        }

    }

    public void setVariableIsntanceProcess(String processInstanceId, String varName, int prioridad) {
        try {
            // this.processEngine = ProcessEngines.getDefaultProcessEngine();
            this.service = processEngine.getRuntimeService();
            this.service.setVariable(processInstanceId, varName, prioridad);
        } catch (Exception e) {
            Log.error("Error");
        }
    }


    public void setTaskPriority(List<Tarea> tareaId, int priority) {
        try {


            // this.processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = this.processEngine.getTaskService();

            for (Tarea item : tareaId) {
                taskService.setPriority(item.getTaskId(), priority);
            }


        } catch (Exception e) {
            Log.error("Error");
        }
    }


    public List<Tarea> listTaskMap(String processId) {
        List<Tarea> result = new ArrayList<>();

        try {
            result = lisTaskProcessInstanceId(processId);

            return result;

        } catch (Exception ex) {
            return null;
        }
    }

    public List<HistoricoTramiteObservacionDto> procesosObservaciones(HistoricoTramiteDto dto) {
        try {
            if (dto.getId() == null) {
                HistoricoTramite tramite = repository.findFirstByTramiteOrderByIdDesc(dto.getTramite());
                if (tramite != null) {
                    dto.setId(tramite.getId());
                } else {
                    return new ArrayList<>();
                }
            }
            return tramiteService.observaciones(dto.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public RespuestaWs verificarExistencia(TipoTramiteDto dto, Integer periodo) {
        List<HistoricoTramite> listaHistorico = tramiteService.listaTramitePeriodo(dto, periodo);
        if (listaHistorico == null) {
            listaHistorico = new ArrayList<>();
        }
        System.out.println("listaHistorico " + listaHistorico.size());

        if (listaHistorico != null && listaHistorico.size() > 0) {
            // this.processEngine = ProcessEngines.getDefaultProcessEngine();
            this.taskService = processEngine.getTaskService();
            List<HistoricTaskInstance> datos = this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(listaHistorico.get(0).getIdProceso()).orderByTaskCreateTime().desc().list();
            if (datos == null) {
                datos = new ArrayList<>();
            }

            System.out.println("datos " + datos.size());

            if (datos != null && datos.size() > 0) {
                if (datos.get(0).getDeleteReason() == null) {
                    return new RespuestaWs(Boolean.TRUE, null, Constantes.datosCorrecto);
                }
            } else {
                return new RespuestaWs(Boolean.TRUE, null, Constantes.datosCorrecto);

            }


        }
        return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
    }

    public List<DocumentoTramiteDto> listarDocumentosXTramite(String tramite) {
        HistoricoTramiteDto historicoTramite = null;
        List<DocumentoTramiteDto> documentos = new ArrayList<>();
        try {
            historicoTramite = tramiteService.consultarXtramite(tramite);
            documentos = documentoRepo.findAllByClaseNameAndIdReferencia("org.ibarra.models.bpm.HistoricoTramite", historicoTramite.getId().toString()).stream().map(data -> documentoTramiteMapper.toDto(data)).collect(Collectors.toList());
            System.out.println("ENTRÓ ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR ");
        }
        return documentos;
    }

    public List<UsuarioTarea> usuarioTareasMovimientos(String processId) {
        return identityLinkRepository.findByProcInstId(processId).stream().map(data -> {
            UsuarioTarea usuarioTarea = new UsuarioTarea();
            usuarioTarea.setUsuario(data.getUserId());
            usuarioTarea.setProcessInstanceId(data.getProcInstId());
            return usuarioTarea;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> consultarTareasComprasPublicas(int pagina, int row, String tipo) {
        try {
            // this.processEngine = ProcessEngines.getDefaultProcessEngine();
            this.taskService = processEngine.getTaskService();
            List<Tramites> result = new ArrayList<>();
            List<String> taskHistoryTmp = new ArrayList<>();
            List<HistoricTaskInstance> taskHistory = new ArrayList<>();
            String processDefinitionKey;

            switch (tipo) {
                case "SOCE":
                    processDefinitionKey = "contratacion_soce";
                    break;
                case "IC":
                    processDefinitionKey = "infima_cuantia_n";
                    break;
                case "REF":
                    processDefinitionKey = "procesoReformaPac";
                    break;
                case "CATE":
                    processDefinitionKey = "contratacion_catalogo";
                    break;
                default:
                    throw new IllegalArgumentException("Tipo desconocido: " + tipo);
            }

            taskHistory = processEngine.getHistoryService().createHistoricTaskInstanceQuery().orderByTaskCreateTime().desc().processDefinitionKey(processDefinitionKey).listPage(pagina, row);

            System.out.println("taskHistory " + taskHistory.size());
            if (taskHistory != null && taskHistory.size() > 0) {
                for (HistoricTaskInstance instance : taskHistory) {
                    Tramites data = new Tramites();
                    if (instance.getProcessDefinitionId() != null) {
                        data.setProcDefId(instance.getProcessDefinitionId());
                    }
                    if (instance.getProcessInstanceId() != null) {
                        data.setProcInstId(instance.getProcessInstanceId());
                    }
                    if (instance.getDurationInMillis() != null) {
                        data.setDuration(instance.getDurationInMillis().toString());

                    }
                    if (instance.getDeleteReason() != null) {
                        data.setDeleteReason(instance.getDeleteReason());
                    }
                    if (instance.getEndTime() != null) {
                        data.setEndTime(instance.getEndTime());
                    }
                    if (instance.getStartTime() != null) {
                        data.setStartTime(instance.getStartTime());
                    }
                    if (instance.getProcessInstanceId() != null) {
                        data.setTramite(tramiteService.consultarHistoricoTramite(instance.getProcessInstanceId()));
                    }
                    data.setEstado(getEstadoTramite(instance));
                    data.setDetalle("S/N");
                    result.add(data);
                }

                List<String> aniadidos = new ArrayList<>();
                List<Tramites> formateados = new ArrayList<>();

                result.forEach(data -> {
                    if (aniadidos != null && aniadidos.size() > 0) {
                        if (!aniadidos.contains(data.getProcInstId())) {
                            formateados.add(data);
                            aniadidos.add(data.getProcInstId());
                        }
                    } else {
                        formateados.add(data);
                        aniadidos.add(data.getProcInstId());
                    }

                });


                PagedListHolder<Object> page = new PagedListHolder(formateados);
                page.setPageSize(row); // number of items per page
                page.setPage(pagina);      // set to first page
                System.out.println("page list size:" + page.getPageList().size());
                List<Object> view = page.getPageList();  // a List which represents the current page which is the sublist
                Map<String, Object> res = new HashMap<>();
                res.put("result", view);
                res.put("totalPage", page.getPageCount());
                res.put("rootSize", page.getPageSize());
                return res;
            }
            return new HashMap<>();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            return new HashMap<>();
        }

    }

    private EstadoTramite getEstadoTramite(HistoricTaskInstance instance) {
        EstadoTramite estado = EstadoTramite.EN_EJECUCIÓN;
        if (instance.getEndTime() != null) {
            if (instance.getDeleteReason() != null) {
                estado = EstadoTramite.ANULADO;
            } else {
                estado = EstadoTramite.FINALIZADO;
            }
        } else {
            estado = EstadoTramite.EN_EJECUCIÓN;
        }
        return estado;
    }

    /*Consulta los tramites de compras públicas por todos los estados*/
    public Map<String, Object> consultaTramitesComprasPublicas(int pagina, int row, String tipo) {
        try {
            Map<String, Object> res;
            res = consultarTareasComprasPublicas(pagina, row, tipo);
            return res;
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            return new HashMap<>();
        }

    }

    public RespuestaWs activarInstanceProcess(String processId) {
        try {
            if (this.processEngine == null) {
                // this.processEngine = ProcessEngines.getDefaultProcessEngine();
            }
            ProcessInstance processInstance = this.processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processId).singleResult();

            if (processInstance == null) {
                return new RespuestaWs(Boolean.FALSE, null, "La instancia de proceso no fue encontrada.");
            }
            this.processEngine.getRuntimeService().activateProcessInstanceById(processId);
            return new RespuestaWs(Boolean.TRUE, null, Constantes.datosCorrecto);
        } catch (ActivitiObjectNotFoundException e) {
            return new RespuestaWs(Boolean.FALSE, null, "La instancia de proceso no fue encontrada.");
        } catch (Exception e) {
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
        }
    }

    public Long numTaskAssignee(String usuario) {

        Long totalTareas = 0L;
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().taskCandidateOrAssigned(usuario).list();

        for (Task task : tasks) {
            String procInstId = task.getProcessInstanceId();

            Integer historicoTramite = repository.countByIdProceso(procInstId);
            if (historicoTramite > 0) {
                totalTareas++;
            }

        }
        return totalTareas;
        //return processEngine.getTaskService().createTaskQuery().taskCandidateOrAssigned(usuario).count();
    }

    public List<ProcessDefinition> getProcessDefinitionsByKey(String processKey) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).orderByProcessDefinitionVersion().desc().list();
    }

    public Integer getProcessDefinitionCountById(String processId) {
        Integer version = repositoryService.createProcessDefinitionQuery().processDefinitionId(processId).singleResult().getVersion();
        if (version != null) {
            return version;
        }
        return 0;
    }

    public HistoricoTramite findByReferencia(HistoricoTramiteDto dto) {
        try {
            return repository.findByReferenciaIdAndReferencia(dto.getReferenciaId(), dto.getReferencia());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DocumentoTramiteDto> listarDocumentosByHistorico(DocumentoTramiteDto dto) {
        try {
            if (dto.getClaseName() != null && dto.getTramite().getId() != null) {
                return documentoRepo.findAllByClaseNameAndTramite_Id(dto.getClaseName(), dto.getTramite().getId()).stream().map(data -> documentoTramiteMapper.toDto(data)).collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public RespuestaWs actualizarDocComunicaciones(RespuestaWs respuestaWs) {
        System.out.println("llega aqui>>>");
        DocumentoTramite dt = documentoRepo.findTopByClaseNameAndIdReferencia("org.ibarra.models.bpm.HistoricoTramite", respuestaWs.getId().toString());
        if (dt != null && dt.getId() != null) {
            dt.setReferenciaDoc(respuestaWs.getData());
            documentoRepo.save(dt);
        }
        return new RespuestaWs(Boolean.TRUE, "Datos actualizados");
    }


    public TareaVariable actualizarVariableTarea(TareaVariable tareaVariable) {
        this.taskService = this.processEngine.getTaskService();
        try {
            taskService.setVariable(tareaVariable.getTaskId(), tareaVariable.getNombreVariable(), tareaVariable.getValorVariable());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tareaVariable;
    }

    public TareaVariable tareasXusuario(TareaVariable tareaVariable) {
        this.taskService = this.processEngine.getTaskService();
        try {
            long asignadas = taskService.createTaskQuery().taskAssignee(tareaVariable.getUsuario()).count();

            System.out.println("Tareas asignadas: " + asignadas);
            tareaVariable.setTareasAsignadas(Integer.parseInt(asignadas + ""));


            long candidatas = taskService.createTaskQuery().taskCandidateUser(tareaVariable.getUsuario()).count();

            System.out.println("Tareas como candidato: " + candidatas);

            tareaVariable.setTareasCandidatas(Integer.parseInt(candidatas + ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tareaVariable;
    }


    public void actualizarDocTramite(Long id, String documento) {
        try {
            DocumentoTramite dt = documentoRepo.getById(id);
            dt.setReferenciaDoc(documento);
            documentoRepo.save(dt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Map<String, Object> consultarTareasVentanilla(String usuario, boolean activa, int pagina, int row, Tarea filtros) {
        try {
            this.taskService = processEngine.getTaskService();
            List<Tarea> result = new ArrayList<>();
            List<Task> tasksAdmin;
            List<HistoricTaskInstance> taskHistory;
            List<String> procesos = null;
            List<String> idProcesos = null;
            boolean realizaFiltro = false;
            Long totalElements = null;
            Integer totalPages = null;

            if (filtros != null) {
                if (filtros.getTramite() != null) {
                    realizaFiltro = true;
                    idProcesos = obtenerProcesosPorTramite(filtros.getTramite());
                }
            }
            if (!activa) {
                taskHistory = obtenerTareasHistoricas(usuario, pagina, row, realizaFiltro, idProcesos);
                for (HistoricTaskInstance instance : taskHistory) {
                    Tarea data = construirTareaDesdeHistorico(instance);
                    if (esTareaVentanilla(data)) {
                        result.add(data);
                    }
                }
            } else {
                if (realizaFiltro && Utils.isNotEmpty(idProcesos)) {
                    totalElements = taskService.createTaskQuery().taskCandidateOrAssigned(usuario).processInstanceIdIn(idProcesos).active().count();

                    tasksAdmin = taskService.createTaskQuery().taskCandidateOrAssigned(usuario).processInstanceIdIn(idProcesos).orderByTaskCreateTime().asc().active().listPage(pagina * row, row);

                    totalPages = (int) Math.ceil((double) totalElements / row);
                } else {
                    totalElements = taskService.createTaskQuery().taskCandidateOrAssigned(usuario).active().count();

                    totalPages = (int) Math.ceil((double) totalElements / row);

                    tasksAdmin = taskService.createTaskQuery().taskCandidateOrAssigned(usuario).orderByTaskCreateTime().asc().active().listPage(pagina * row, row);
                }

                for (Task instance : tasksAdmin) {
                    Tarea data = construirTareaDesdeActiva(instance);
                    if (esTareaVentanilla(data)) {
                        result.add(data);
                    }
                }
            }

            return construirRespuestaPaginada(result, pagina, row, totalElements, totalPages);

        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            return null;
        }
    }


    private List<String> obtenerProcesosPorTramite(HistoricoTramiteDto dto) {
        List<HistoricoTramite> all;
        HistoricoTramite entity = historicoTramiteMapper.toEntity(dto);

        if (dto.getFechaInicio() == null || dto.getFechaFin() == null) {
            all = repository.findAll(Example.of(entity, ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)));
        } else {
            all = repository.findAllByFechaIngresoBetween(dto.getFechaInicio(), dto.getFechaFin());
        }

        return Utils.isNotEmpty(all) ? all.stream().map(HistoricoTramite::getIdProceso).filter(Objects::nonNull).collect(Collectors.toList()) : null;
    }

    private List<HistoricTaskInstance> obtenerTareasHistoricas(String usuario, int pagina, int row, boolean filtrar, List<String> idProcesos) {
        HistoricTaskInstanceQuery query = processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(usuario).orderByTaskCreateTime().asc().orderByTaskId().desc();

        if (filtrar && Utils.isNotEmpty(idProcesos)) {
            query.processInstanceIdIn(idProcesos);
        } else if (filtrar) {
            return new ArrayList<>();
        }

        return query.listPage(pagina * row, row);
    }

    private Tarea construirTareaDesdeHistorico(HistoricTaskInstance instance) {
        Tarea data = new Tarea();
        data.setFormKey(instance.getFormKey());
        data.setTaskId(instance.getId());
        data.setTaskDefinitionKey(instance.getTaskDefinitionKey());
        data.setTaskName(instance.getName());
        data.setAssignee(instance.getAssignee());
        data.setPriority(instance.getPriority());
        data.setFechaInicio(instance.getStartTime());
        data.setEstado(estadoTareaService.obtenerEstado(instance));

        return completarInfoTarea(data, instance.getProcessInstanceId());
    }

    private Tarea construirTareaDesdeActiva(Task instance) {
        Tarea data = new Tarea();
        data.setFormKey(instance.getFormKey());
        data.setTaskId(instance.getId());
        data.setTaskDefinitionKey(instance.getTaskDefinitionKey());
        data.setTaskName(instance.getName());
        data.setAssignee(instance.getAssignee());
        data.setPriority(instance.getPriority());
        data.setFechaInicio(instance.getCreateTime());
        data.setEstado(estadoTareaService.obtenerEstado(instance));

        return completarInfoTarea(data, instance.getProcessInstanceId());
    }

    private Tarea completarInfoTarea(Tarea data, String processInstanceId) {
        HistoricProcessInstance procInstance = processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (procInstance == null) {
            return data;
        }
        String realProcessId = Optional.ofNullable(procInstance.getSuperProcessInstanceId()).orElse(procInstance.getId());
        data.setProcessInstanceID(realProcessId);
        data.setTramite(tramiteService.consultarHistoricoTramite(realProcessId));

        if (data.getTramite() != null && data.getTramite().getId() != null) {
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(procInstance.getProcessDefinitionId()).singleResult();
            data.setVersion(pd.getVersion());

            ProcessDefinition latest = repositoryService.createProcessDefinitionQuery().processDefinitionKey(data.getTramite().getTipoTramite().getActivitykey()).latestVersion().active().singleResult();
            data.setLatestVersion(latest.getVersion());

            HistoricoTramiteObservacion ob = historicoTramiteObservacionRepo.findFirstByTramite_idOrderByIdDesc(data.getTramite().getId());
            if (ob != null) {
                data.setObservacionUsuario(ob.getObservacion());
            }
        }

        return data;
    }

    private boolean esTareaVentanilla(Tarea data) {
        return data.getTramite() != null && data.getTramite().getTipoTramite() != null && Boolean.TRUE.equals(data.getTramite().getTipoTramite().getVentanillaPublica());
    }

    private Map<String, Object> construirRespuestaPaginada(List<Tarea> result, int pagina, int row, Long totalElements, Integer totalPages) {
        PagedListHolder<Object> page = new PagedListHolder<>(new ArrayList<>(result));
        page.setPageSize(row);
        page.setPage(pagina);
        Map<String, Object> res = new HashMap<>();
        res.put("result", page.getPageList());
        res.put("totalPage", totalPages != null ? totalPages : page.getPageCount());
        res.put("rootSize", totalElements != null ? totalElements : page.getNrOfElements());
        return res;
    }
}
