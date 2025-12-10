package org.ibarra.service;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.ibarra.util.Utils;
import org.ibarra.util.model.EstadoTarea;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EstadoTareaService {

    public EstadoTarea obtenerEstado(Object tasko) {
        EstadoTarea estadoTarea = EstadoTarea.ASSIGNED_ON_TIME;
        Date fechaActual = new Date();
        if (tasko instanceof Task task) {
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
        } else {
            HistoricTaskInstance task = (HistoricTaskInstance) tasko;
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
        }

        return estadoTarea;
    }

}
