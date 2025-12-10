package org.ibarra.service;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.ibarra.conf.AppProps;
import org.ibarra.dto.HistoricoTramiteDto;
import org.ibarra.dto.RespuestaWs;
import org.ibarra.dto.Tarea;
import org.ibarra.entity.HistoricoTramite;
import org.ibarra.entity.HistoricoTramiteObservacion;
import org.ibarra.mapper.HistoricoTramiteMapper;
import org.ibarra.repository.HistoricoTramiteObservacionRepo;
import org.ibarra.repository.HistoricoTramiteRepo;
import org.ibarra.util.Constantes;
import org.ibarra.util.Utils;
import org.ibarra.util.model.BusquedaDinamica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BandejaTareaService {

    @Autowired
    private RestService restService;
    @Autowired
    private AppProps appProps;

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoricoTramiteRepo repository;
    @Autowired
    private HistoricoTramiteObservacionRepo historicoTramiteObservacionRepo;
    @Autowired
    private HistoricoTramiteMapper historicoTramiteMapper;

    @Autowired
    private EstadoTareaService estadoTareaService;
    @Autowired
    private HistoricoTramiteService tramiteService;


    public Map<String, Object> consultarTareas(String usuario, boolean activa, int pagina, int row, Tarea filtros) {
        try {

            List<Tarea> result = new ArrayList<>();
            List<Task> tasksAdmin;
            List<HistoricTaskInstance> taskHistory;
            List<String> procesos = null;
            List<String> idProcesos = null;
            Boolean realizaFilro = false;

            Long totalElements = null;
            Integer totalPages = null;

            if (filtros != null) {

                if (filtros.getTramite() != null) {
                    List<HistoricoTramite> all = null;
                    HistoricoTramiteDto dto = filtros.getTramite();
                    HistoricoTramite dHistoricoTramite = historicoTramiteMapper.toEntity(dto);
                    if (dto.getFechaInicio() == null || dto.getFechaFin() == null) {
                        all = this.repository.findAll(Example.of(dHistoricoTramite, ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)));
                    } else {
                        System.out.println(dto.getFechaInicio());
                        System.out.println(dto.getFechaFin());
                        all = repository.findAllByFechaIngresoBetween(dto.getFechaInicio(), dto.getFechaFin());
                    }
                    if (Utils.isNotEmpty(all)) {
                        realizaFilro = true;
                        idProcesos = all.stream().map(HistoricoTramite::getIdProceso).filter(Objects::nonNull).collect(Collectors.toList());
                    }
                }
            }
            if (!activa) {
                HistoricTaskInstanceQuery task = processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(usuario).orderByTaskCreateTime().asc();
                if (Utils.isNotEmpty(idProcesos)) {
                    task.processInstanceIdIn(idProcesos);
                }
                if (realizaFilro) {
                    if (Utils.isNotEmpty(idProcesos)) {
                        taskHistory = processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(usuario).processInstanceIdIn(idProcesos).orderByTaskCreateTime().asc().listPage(pagina, row);
                    } else {
                        taskHistory = new ArrayList<>();
                    }
                } else {
                    taskHistory = processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(usuario).orderByTaskCreateTime().asc().listPage(pagina, row);
                }
                if (!taskHistory.isEmpty()) {
                    for (HistoricTaskInstance instance : taskHistory) {
                        Tarea data = new Tarea();
                        data.setFormKey(instance.getFormKey());
                        data.setTaskId(instance.getId());
                        data.setTaskDefinitionKey(instance.getTaskDefinitionKey());
                        data.setTaskName(instance.getName());
                        data.setAssignee(instance.getAssignee());
                        data.setPriority(instance.getPriority());
                        data.setFechaInicio(instance.getStartTime());
                        data.setEstado(estadoTareaService.obtenerEstado(instance));
                        HistoricProcessInstance processInstanceId = processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getProcessInstanceId()).singleResult();
                        if (processInstanceId.getSuperProcessInstanceId() != null) {
                            data.setTramite(tramiteService.consultarHistoricoTramite(processInstanceId.getSuperProcessInstanceId()));
                            data.setProcessInstanceID(processInstanceId.getSuperProcessInstanceId());
                        } else {
                            data.setTramite(tramiteService.consultarHistoricoTramite(processInstanceId.getId()));
                            data.setProcessInstanceID(processInstanceId.getId());
                        }
                        if (data.getTramite() != null && data.getTramite().getId() != null) {
                            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstanceId.getProcessDefinitionId()).singleResult();
                            data.setVersion(pd.getVersion());
                            ProcessDefinition latVers = repositoryService.createProcessDefinitionQuery().processDefinitionKey(data.getTramite().getTipoTramite().getActivitykey()).latestVersion().active().singleResult();
                            data.setVersion(pd.getVersion());
                            data.setLatestVersion(latVers.getVersion());
                            HistoricoTramiteObservacion ob = historicoTramiteObservacionRepo.findFirstByTramite_idOrderByIdDesc(data.getTramite().getId());
                            if (ob != null) {
                                if (!ob.getUsuarioCreacion().equals(Constantes.PORTAL_WEB)) { //observacion x defecto
                                    data.setObservacionUsuario(ob.getObservacion());
                                } else {
                                    data.setObservacionUsuario("-");
                                }
                            }
//                        }

                        }
                        result.add(data);
                    }
                }
            } else {
                System.out.println(realizaFilro);
                if (realizaFilro) {
                    if (Utils.isNotEmpty(idProcesos)) {
                        totalElements = processEngine.getTaskService().createTaskQuery().taskCandidateOrAssigned(usuario).processInstanceIdIn(idProcesos).active().count();

                        tasksAdmin = processEngine.getTaskService().createTaskQuery().taskCandidateOrAssigned(usuario).processInstanceIdIn(idProcesos).orderByTaskCreateTime().asc().active().listPage(pagina * row, row);

                        totalPages = (int) Math.ceil((double) totalElements / row);

                    } else {
                        tasksAdmin = new ArrayList<>();
                    }
                } else {
                    totalElements = processEngine.getTaskService().createTaskQuery().taskCandidateOrAssigned(usuario).active().count();
                    totalPages = (int) Math.ceil((double) totalElements / row);

                    tasksAdmin = processEngine.getTaskService().createTaskQuery().taskCandidateOrAssigned(usuario).orderByTaskCreateTime().asc().active().listPage(pagina * row, row);

                }
                if (Utils.isNotEmpty(tasksAdmin)) {
                    for (Task instance : tasksAdmin) {
                        Tarea data = new Tarea();
                        data.setFormKey(instance.getFormKey());
                        data.setTaskId(instance.getId());
                        data.setTaskDefinitionKey(instance.getTaskDefinitionKey());
                        data.setTaskName(instance.getName());
                        data.setAssignee(instance.getAssignee());
                        data.setPriority(instance.getPriority());
                        data.setFechaInicio(instance.getCreateTime());
                        data.setEstado(estadoTareaService.obtenerEstado(instance));
                        HistoricProcessInstance processInstanceId = processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getProcessInstanceId()).singleResult();
                        if (processInstanceId.getSuperProcessInstanceId() != null) {
                            data.setTramite(tramiteService.consultarHistoricoTramite(processInstanceId.getSuperProcessInstanceId()));
                            data.setProcessInstanceID(processInstanceId.getSuperProcessInstanceId());
                        } else {
                            data.setTramite(tramiteService.consultarHistoricoTramite(processInstanceId.getId()));
                            data.setProcessInstanceID(processInstanceId.getId());
                        }
                        if (data.getTramite() != null && data.getTramite().getId() != null) {
                            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstanceId.getProcessDefinitionId()).singleResult();
                            data.setVersion(pd.getVersion());
                            ProcessDefinition latVers = repositoryService.createProcessDefinitionQuery().processDefinitionKey(data.getTramite().getTipoTramite().getActivitykey()).latestVersion().active().singleResult();
                            data.setVersion(pd.getVersion());
                            data.setLatestVersion(latVers.getVersion());
                            HistoricoTramiteObservacion ob = historicoTramiteObservacionRepo.findFirstByTramite_idOrderByIdDesc(data.getTramite().getId());
                            if (ob != null) {
                                if (!ob.getUsuarioCreacion().equals(Constantes.PORTAL_WEB)) { //observacion x defecto
                                    data.setObservacionUsuario(ob.getObservacion());
                                } else {
                                    data.setObservacionUsuario("-");
                                }
                            }

                            result.add(data);
                        }
                    }
                }
            }
            PagedListHolder<Object> page = new PagedListHolder(result);
            page.setPageSize(row);
            page.setPage(pagina);
            List<Object> view = page.getPageList();
            Map<String, Object> res = new HashMap<>();
            res.put("result", view);


            if (totalElements != null && totalPages != null) {
                res.put("totalPage", totalPages);
                res.put("rootSize", totalElements);
            } else {
                res.put("totalPage", page.getPageCount());
                res.put("rootSize", page.getPageSize());
            }


            return res;
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace(System.out);
            return null;
        }
    }

}
