package org.ibarra.service;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcesoEliminadorService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private CacheManager cacheManager;

    public ProcesoEliminacionResultado eliminarProcesos(List<String> processIds) {
        List<String> eliminados = new ArrayList<>();
        List<String> errores = new ArrayList<>();

        for (String procId : processIds) {
            try {
                ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(procId)
                        .singleResult();

                if (instance != null) {
                    runtimeService.deleteProcessInstance(procId, "Eliminado vía servicio REST");
                    System.out.println("Eliminados!!!1");
                } else {
                    historyService.deleteHistoricProcessInstance(procId);
                }

                eliminados.add(procId);
            } catch (Exception e) {
                errores.add("Error con " + procId + ": " + e.getMessage());
            }
        }

        return new ProcesoEliminacionResultado(eliminados, errores);
    }

    public record ProcesoEliminacionResultado(List<String> eliminadosOk, List<String> erroresEncontrados) {}

}
