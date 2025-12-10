package org.ibarra.service;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.history.HistoricTaskInstance;
import org.ibarra.dto.Tarea;
import org.ibarra.util.model.EstadoTarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TareaUsuarioService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    public List<Tarea> listarTareasPorUsuarioActivas(String usuarioId, int page, int size, Tarea filtros) {
        Map<String, Object> resultMap = new HashMap<>();

        int offset = page * size;
        List<Tarea> resultado = new ArrayList<>();

        // Tareas activas
        List<Task> tareasActivas = taskService.createTaskQuery()
                .or()
                .taskAssignee(usuarioId)
                .taskCandidateUser(usuarioId)
                .endOr()
                .active()
                .orderByTaskCreateTime().desc()
                .list();

        for (Task task : tareasActivas) {
            Tarea tarea = new Tarea();
            tarea.setTaskId(task.getId());
            tarea.setTaskDefinitionKey(task.getTaskDefinitionKey());
            tarea.setTaskName(task.getName());
            tarea.setAssignee(task.getAssignee());
            tarea.setPriority(task.getPriority());
            tarea.setProcessInstanceID(task.getProcessInstanceId());
            tarea.setFormKey(task.getFormKey());
            tarea.setFechaInicio(task.getCreateTime());
            tarea.setEstado(determinarEstadoTareaActiva(task));
            resultado.add(tarea);
        }


        return resultado;
    }

    public List<Tarea> listarTareasPorUsuarioCompletadas(String usuarioId, int page, int size, Tarea filtros) {
        Map<String, Object> resultMap = new HashMap<>();

        int offset = page * size;
        List<Tarea> resultado = new ArrayList<>();


        // Tareas completadas
        List<HistoricTaskInstance> tareasCompletadas = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(usuarioId)
                .taskCandidateUser(usuarioId)

                .finished()
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

        for (HistoricTaskInstance hist : tareasCompletadas) {
            Tarea tarea = new Tarea();
            tarea.setTaskId(hist.getId());
            tarea.setTaskDefinitionKey(hist.getTaskDefinitionKey());
            tarea.setTaskName(hist.getName());
            tarea.setAssignee(hist.getAssignee());
            tarea.setPriority(hist.getPriority());
            tarea.setProcessInstanceID(hist.getProcessInstanceId());
            tarea.setFechaInicio(hist.getStartTime());
            tarea.setFechaFin(hist.getEndTime());
            tarea.setEstado(determinarEstadoTareaCompletada(hist));
            resultado.add(tarea);
        }


        return resultado;

    }


    public long contarTareasActivas(String usuarioId) {
        return taskService.createTaskQuery()
                .or()
                .taskAssignee(usuarioId)
                .taskCandidateUser(usuarioId)
                .endOr()
                .active()
                .count();
    }

    public long contarTareasCompletadas(String usuarioId) {
        return historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(usuarioId)
                .finished()
                .count();
    }

    private EstadoTarea determinarEstadoTareaActiva(Task task) {
        if (task.getDueDate() == null) {
            return EstadoTarea.ASSIGNED_ON_TIME;
        }

        Date now = new Date();
        long tiempoRestante = task.getDueDate().getTime() - now.getTime();

        if (tiempoRestante < 0) {
            return EstadoTarea.OUT_OF_TIME;
        } else if (tiempoRestante <= 24 * 60 * 60 * 1000) { // menos de 24h
            return EstadoTarea.NEARING_DEADLINE;
        } else {
            return EstadoTarea.ASSIGNED_ON_TIME;
        }
    }

    private EstadoTarea determinarEstadoTareaCompletada(HistoricTaskInstance hist) {
        if (hist.getDueDate() == null || hist.getEndTime() == null) {
            return EstadoTarea.COMPLETED_ON_TIME;
        }

        if (hist.getEndTime().after(hist.getDueDate())) {
            return EstadoTarea.COMPLETED_LATE;
        } else {
            return EstadoTarea.COMPLETED_ON_TIME;
        }
    }

}
