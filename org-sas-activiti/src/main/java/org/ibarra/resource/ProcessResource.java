package org.ibarra.resource;

import org.ibarra.dto.HistoricoTramiteDto;
import org.ibarra.dto.ProcessDeploy;
import org.ibarra.dto.RespuestaWs;
import org.ibarra.dto.Tarea;
import org.ibarra.service.HistoricoTramiteService;
import org.ibarra.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * SI SE VA A UTILIZA LA DEPENDENCIA DE LA API-REST DEL ACTIVITI DEBE DESCONMENTAR LA DEPENDEICA DEL activiti-spring-boot-starter-rest-api
 * EN EL ARCHIVO pom.xml DONDE PARA CONSUMIR ESOS API TIENE AUTENTICARSE EL USUARIOS es sisapp AND pass sisapp98, SI NO LO VAN A USAR ESA
 * DEPENPENDENCIA NO NECESITAN AUTHENTICACION PARA PODER CONSUMIR LAS APIS-REST QUE ESTAN EN ESTA CLASE....!
 */
@Controller
@RequestMapping(value = "process/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProcessResource {
    @Autowired
    private ProcessService service;
    @Autowired
    private HistoricoTramiteService historicoTramiteService;
    private Logger log = LoggerFactory.getLogger(ProcessResource.class);


    /**
     * Este metodo nos permite terminar la instancia de un proceso, donde se requiere pasar el id del porceos y una cadena de texto que sirve
     * para conocer cual fue el motivo de la terminación de dicho proceso
     */

    @PostMapping("delete/processInstance/{processId}")
    public ResponseEntity<?> deleteProcessIntance(@PathVariable String processId, @RequestBody String observaciones) {
        try {

            return new ResponseEntity<>(service.deleteInstanceProcess(processId, observaciones), HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Este metodo nos permite reasignar la tarea por X motivo, donde se requiere que id de la tarea y el usuario al que se lo va a asignar
     */
    @PostMapping("reasing/task/{taskId}/{user}")
    public ResponseEntity<?> reassignTask(@PathVariable String taskId, @PathVariable String user) {
        try {
            this.service.reassignTask(taskId, user);
            log.info("REAGSINAMIENTO EXITOSO");
            return new ResponseEntity<>(new RespuestaWs(Boolean.TRUE, null, null), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new RespuestaWs(Boolean.FALSE, null, null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Este metodo nos permite listar las tarea de un proceso, donde se requiere el id de la instancia del proceso y los parametos
     * como el nummero de pagina y el tamanio, para trabajar con lo lazy
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "list/task/{processId}")
    public ResponseEntity<?> TaskActiveProcess(@PathVariable String processId) {
        try {
            log.info("SUCCESS");
            return new ResponseEntity<>(this.service.taskActive(processId, null), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("ERROR");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Este nos permite obtener un diagrama en tiempo real o tb un diagrama de una instanci de proceso que ya finalizo
     * donde se requiere el id de la instancia del proceso
     */
    @GetMapping(value = "current/diagram/especially/{processId}")
    public ResponseEntity<byte[]> currentDiagramProcessEspecially(@PathVariable String processId) {
        try {
            log.info("DIAGRAM SUCCESS");
            byte[] imgen_array = (service.diagramEspeciality(processId)).readAllBytes();


            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + processId + "\"")
                    .body(imgen_array);

        } catch (Exception ex) {
            log.error("ERROR ", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("admin/list/process/deploy")
    public ResponseEntity<List<ProcessDeploy>> listProcessDeploy() {
        try {

            log.info("SUCESS");
            return new ResponseEntity<>(service.listProcessDeploy(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("admin/list/process/history")
    public ResponseEntity<?> listProcessHistory(@RequestParam int page, @RequestParam int size) {
        try {
            Map<String, Object> result = service.listProcessHistory(page, size);
            HttpHeaders header = new HttpHeaders();
            header.add("totalPage", result.get("totalPage").toString());
            header.add("totalSize", result.get("rootSize").toString());
            return new ResponseEntity<>(result.get("result"), header, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("admin/process/history")
    public ResponseEntity<?> listProcessHistoryNotPage() {
        try {
            return new ResponseEntity<>(service.listProcessHistory(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value = "admin/prioridad/tramite")
    public ResponseEntity<?> getVaribaleProcessInstance(String processInstanceId, String varName) {
        Object result = service.getVaribaleInstanceProcess(processInstanceId, varName);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(value = "admin/variable/tramite/{processInstanceId}/{varName}")
    public ResponseEntity<?> getVaribaleProcessInstanceTramite(@PathVariable("processInstanceId") String processInstanceId, @PathVariable("varName") String varName) {
        Object result = service.getVaribaleInstanceProcess(processInstanceId, varName);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PutMapping(value = "admin/prioridad/tramite")
    public ResponseEntity<?> SetVaribaleProcessInstance(String processInstanceId, String varName, int prioridad) {
        try {
            service.setVariableIsntanceProcess(processInstanceId, varName, prioridad);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "admin/tareas/list/{processId}")
    public ResponseEntity<?> listTareasActivas(@PathVariable String processId) {
        try {

            return new ResponseEntity<>(service.listTaskMap(processId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "admin/observaciones"
            , produces = MediaType.APPLICATION_JSON_VALUE
            , consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> observacionesProcesos(@RequestBody HistoricoTramiteDto dto) {
        try {
            return new ResponseEntity<>(service.procesosObservaciones(dto), HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "admin/tareas/prioridad")
    public ResponseEntity<?> prioridadTareasTramite(@RequestBody List<Tarea> tareas, int prioridad) {

        service.setTaskPriority(tareas, prioridad);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping(value = "admin/usuarios/{processId}")
    public ResponseEntity<?> getProcessInstance(@PathVariable String processId) {
        try {
            return new ResponseEntity<>(service.usuarioTareasMovimientos(processId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("activate/processInstance/{processId}")
    public ResponseEntity<?> deleteProcessIntance(@PathVariable String processId) {
        try {
            return new ResponseEntity<>(service.activarInstanceProcess(processId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("numTaskAssignee/{usuario}")
    public ResponseEntity<?> numTaskAssignee(@PathVariable String usuario) {
        try {
            return new ResponseEntity<>(service.numTaskAssignee(usuario), HttpStatus.OK);
        } catch (Exception e) {
            log.error("ERROR " + e.getMessage());
            return new ResponseEntity<>(0L, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "historicoTramite/actualizar", method = RequestMethod.POST)
    public ResponseEntity<?> actualizarHistoricoTramite(@RequestBody HistoricoTramiteDto dto) {
        return new ResponseEntity<>(historicoTramiteService.actualizarHistoricoTramite(dto), HttpStatus.OK);
    }

    @RequestMapping(value = "historicoTramite/guardarObservacion", method = RequestMethod.POST)
    public ResponseEntity<?> actualizarHistoricoTramite(@RequestBody Tarea dto) {
        return new ResponseEntity<>(historicoTramiteService.guardarObservacion(dto), HttpStatus.OK);
    }

    @GetMapping("processDefinition/versions/{key}")
    public ResponseEntity<?> getProcessDefinitionsByKey(@PathVariable String key) {
        try {
            return new ResponseEntity<>(service.getProcessDefinitionsByKey(key), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0L, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("processDefinition/versions-by-key/{id}")
    public ResponseEntity<?> getProcessDefinitionVersionsByKey(@PathVariable String id) {
        try {
            return new ResponseEntity<>(service.getProcessDefinitionCountById(id), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener versiones para key: " + id, e);
            return ResponseEntity.badRequest().build();
        }
    }
}
