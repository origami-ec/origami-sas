package org.ibarra.resource;

import jakarta.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.ibarra.dto.Process;
import org.ibarra.dto.*;
import org.ibarra.service.ProcessService;
import org.ibarra.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "process/")
//@RolesAllowed({"*"})
public class TramitesResource {
    private Logger log = java.util.logging.Logger.getLogger(TramitesResource.class.getName());
    @Autowired
    private ProcessService service;

    /**
     * implementar una instancia del Bpmn, se requiers pasar un modelo de PrcocesModel (data), donde se obtendra la ruta de la carptea donde
     * se ecuentra el archivo.bpmn para poderlo deployar.
     */

    @PostMapping("desplegarProceso")
    public ResponseEntity<?> processImplementation(@RequestBody TipoTramiteDto data) {
        try {
            log.info("SUCCESS DEPLOY");
            return new ResponseEntity<>(service.processImplement(data), HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
//            System.out.println(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }


    /**
     * realizar una instancia de un proceso, donde es necesario para un modelo de datos ProcessModel (data), que se obtendra el id o activityKey
     * del procesos como tal, para poder realizar la instancia del mismo valga la redundacia y ademas se tendria que pasar los parametro
     * que va a recibir la primera tarea por ejemplo la primera tarea tiene la variable ${usuario} ===> example:
     * entonces debemos llenar esos datos con el Map<String,Objet> param ======> para.put("usuario","luisSuarez")
     */
    @PostMapping("crearTramite")
    public ResponseEntity<?> processInstance(@RequestBody Process data) {
        try {
            log.info("SUCCESS " + data.getTipoTramite().getAbreviatura());
            return new ResponseEntity<>(service.processStart(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Este metodo nos devuleve una lista de todas la tareas donde requiere un boolean active para poder verificar si se requiere consultar
     * tarea historico con active=false, y tareas actuales con active=true, ademas de pasar los demas parametos
     * como page y size para los lazy
     */


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "admin/consultarTareas")
    public ResponseEntity<List<Tramites>> consultarTareas(@RequestParam int page, @RequestParam int size, @Valid Tramites tramite) {
        try {
            Map<String, Object> result = this.service.consultarTareasTodos(page, size, tramite);
            if (result == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.add("totalPages", result.get("totalPage").toString());
            responseHeader.add("rootSize", result.get("rootSize").toString());
            List<Tramites> data = (List<Tramites>) result.get("result");
            log.info("SUCESS ");
            return new ResponseEntity<>(data, responseHeader, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.SEVERE, "ERROR", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "admin/consultarTareasVentanilla")
    public ResponseEntity<List<Tramites>> consultarTareasVentanillaPublica(@RequestParam int page, @RequestParam int size, @Valid Tramites tramite) {
        try {
            log.log(Level.SEVERE, "ERROR");
            Map<String, Object> result = this.service.consultarTareaTodasVentanillaPublica(page, size, tramite);
            if (result == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.add("totalPages", result.get("totalPage").toString());
            responseHeader.add("rootSize", result.get("rootSize").toString());
            List<Tramites> data = (List<Tramites>) result.get("result");
            log.info("SUCESS");
            return new ResponseEntity<>(data, responseHeader, HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "admin/consultarTareasAtencionCiudadana")
    public ResponseEntity<List<Tramites>> consultarTareasAtencionCiudadana(@RequestParam int page, @RequestParam int size, @Valid Tramites tramite) {
        try {
            log.log(Level.SEVERE, "ERROR");
            Map<String, Object> result = this.service.consultarTareaTodasAtencionCiudadana(page, size, tramite);
            if (result == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.add("totalPages", result.get("totalPage").toString());
            responseHeader.add("rootSize", result.get("rootSize").toString());
            List<Tramites> data = (List<Tramites>) result.get("result");
            log.info("SUCESS");
            return new ResponseEntity<>(data, responseHeader, HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "consultaTramite/{usuario}")
    public ResponseEntity<List<Tramites>> consultaTramitesUsuario(@RequestParam int page, @RequestParam int size, @PathVariable String usuario, @Valid Tramites tramite) {
        try {
            log.log(Level.INFO, "paranmetros {} , {}, {}", new Object[]{page, size, usuario});
            Map<String, Object> result = this.service.consultaTramitesUsuario(page, size, usuario, tramite);
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.add("totalPages", result.get("totalPage").toString());
            responseHeader.add("rootSize", result.get("rootSize").toString());
            List<Tramites> data = (List<Tramites>) result.get("result");
            log.info("SUCESS");
            return new ResponseEntity<>(data, responseHeader, HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("consultarTarea")
    public ResponseEntity<?> consultarTarea(@RequestBody Tarea dto) {
        try {
            Tarea tarea;
            if (Utils.isNotEmptyString(dto.getTaskId())) {
                System.out.println("tarea 1 ");
                log.info("consultarTarea dto.getTaskId())");
                tarea = service.taskActive(dto.getTaskId(), dto.getAssignee());
            } else if (dto.getTramite() != null) {
                System.out.println("tarea 2 ");
                log.info("consultarTarea dto.getTramite())");
                tarea = service.taskActiveXtramite(dto);
            } else {
                System.out.println("tarea 2 ");
                tarea = null;
            }
            System.out.println("taraea ");
            return new ResponseEntity<>(tarea, HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            System.out.println(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("consultarXtramite")
    public ResponseEntity<?> consultarXtramite(@RequestBody RespuestaWs dto) {
        try {
            log.info("consultarXtramite");
            return new ResponseEntity<>(service.consultarXtramite(dto.getData(), null), HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            System.out.println(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("consultarHistoricoXtramite")
    public ResponseEntity<?> consultarHistoricoXtramite(@RequestBody RespuestaWs dto) {
        try {
            log.info("consultarHistoricoXtramite  " + dto.getData());
            return new ResponseEntity<>(service.consultarHistoricoXtramite(dto.getData()), HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            System.out.println(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("completar/tarea")
    public ResponseEntity<?> taskComplete(@RequestBody Tarea tarea) {
        try {

            log.info("SUCCESS: " + tarea.toString());
            return new ResponseEntity<>(service.taskComplete(tarea), HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("actualizarVariableTarea")
    public ResponseEntity<?> actualizarVariableTarea(@RequestBody TareaVariable tarea) {
        try {
            log.info("SUCCESS: " + tarea.toString());
            return new ResponseEntity<>(service.actualizarVariableTarea(tarea), HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Este metodo nos permite obter el diagrama del proceso actual, donde requiere pasar el id del proceso y el nombre de la ruta del imagen
     */
    @GetMapping(value = "consultar/diagrama", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> currentDiagramProcess(@RequestParam String dto, @RequestParam String data) {
        try {
            log.info("DIAGRAM SUCCESS");
            byte[] imgen_array = IOUtils.toByteArray(service.processDiagram(dto, data));
            return ResponseEntity.ok().body(imgen_array);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "ERROR", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("guardar/documentos")
    public ResponseEntity<?> guardarDocumentos(@RequestBody DocumentoTramiteDto dto) {
        return new ResponseEntity<>(service.guardarDocumentos(dto), HttpStatus.OK);
    }

    @PostMapping("eliminar/documento")
    public ResponseEntity<?> eliminarDocumento(@RequestBody DocumentoTramiteDto dto) {
        try {

            return new ResponseEntity<>(service.eliminarDocumentos(dto), HttpStatus.OK);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "ERROR", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "listar/documentos", produces = MediaType.APPLICATION_JSON_VALUE
            , consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> listarDocumentos(@RequestBody DocumentoTramiteDto dto) {
        try {
            return new ResponseEntity<>(service.listarDocumentos(dto), HttpStatus.OK);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "ERROR", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("buscar/documento/{code}")
    public ResponseEntity<?> buscarDocumento(@PathVariable Long code) {
        log.info("buscando documento " + code);

        return new ResponseEntity<>(service.buscarDocumento(code), HttpStatus.OK);


    }

    @PostMapping("listar/requisitos")
    public ResponseEntity<?> listarDocumentos(@RequestBody TipoTramiteDto dto) {
        try {

            return new ResponseEntity<>(service.listarRequisitos(dto), HttpStatus.OK);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "ERROR", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("listar/sub/requisitos/{code}")
    public ResponseEntity<?> listarSubDocumentos(@PathVariable Long code) {
        try {

            return new ResponseEntity<>(service.listarSubequisitos(code), HttpStatus.OK);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "ERROR", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("verificar/existencia/tramite/periodo/{periodo}")
    public ResponseEntity<?> listarDocumentos(@RequestBody TipoTramiteDto dto, @PathVariable Integer periodo) {
        try {
            return new ResponseEntity<>(service.verificarExistencia(dto, periodo), HttpStatus.OK);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "ERROR", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("consultar/documentosXtarea/{tramite}")
    public ResponseEntity<?> listarDocumentosXTramite(@PathVariable String tramite) {
        try {
            return new ResponseEntity<>(service.listarDocumentosXTramite(tramite), HttpStatus.OK);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "ERROR", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "consultaTramite/compras/{tipo}")
    public ResponseEntity<List<Tramites>> consultaTramitesComprasPublicas(@RequestParam int page, @RequestParam int size, @PathVariable String tipo, @Valid Tramites tramite) {
        try {
            log.log(Level.INFO, "paranmetros {} , {}, {}", new Object[]{page, size});
            Map<String, Object> result = this.service.consultaTramitesComprasPublicas(page, size, tipo);
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.add("totalPages", result.get("totalPage").toString());
            responseHeader.add("rootSize", result.get("rootSize").toString());
            List<Tramites> data = (List<Tramites>) result.get("result");
            log.info("SUCESS");
            return new ResponseEntity<>(data, responseHeader, HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/consultarTareas/{periodo}/{tipoTramite}")
    public ResponseEntity<List<Tramites>> consultarTareas(@Valid @ModelAttribute("tramites") Tramites filtro, @PathVariable(required = false) String tipoTramite, @PathVariable Integer periodo, @RequestParam int page, @RequestParam int size) {
        try {
            log.info("SUCESS " + filtro.toString());
            Map<String, Object> result = this.service.consultarTareasTipoTramiteAnio(filtro, tipoTramite, periodo, page, size);
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.add("totalPages", result.get("totalPage").toString());
            responseHeader.add("rootSize", result.get("rootSize").toString());
            List<Tramites> data = (List<Tramites>) result.get("result");
            log.info("SUCESS");
            return new ResponseEntity<>(data, responseHeader, HttpStatus.OK);

        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("listar/historicoTramite")
    public ResponseEntity<?> cargarHistoricoTramite(@RequestBody HistoricoTramiteDto dto) {
        try {
            return new ResponseEntity<>(service.findByReferencia(dto), HttpStatus.OK);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "ERROR", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "listar/documentos/historico", produces = MediaType.APPLICATION_JSON_VALUE
            , consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> listarDocumentosByHistorico(@RequestBody DocumentoTramiteDto dto) {
        try {
            return new ResponseEntity<>(service.listarDocumentosByHistorico(dto), HttpStatus.OK);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "ERROR", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "documentoTramite/actualizar", method = RequestMethod.POST)
    public ResponseEntity<?> actualizarDocComunicaciones(@RequestBody RespuestaWs respuestaWs) {
        return new ResponseEntity<>(service.actualizarDocComunicaciones(respuestaWs), HttpStatus.OK);
    }

    @PostMapping("tareasXusuario")
    public ResponseEntity<?> tareasXusuario(@RequestBody TareaVariable tarea) {
        try {
            log.info("tareasXusuario: " + tarea.toString());
            return new ResponseEntity<>(service.tareasXusuario(tarea), HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "documentoTramite/actualizarDoc/{id}/documento/{documento}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> actualizarDocTramite(@PathVariable Long id, @PathVariable String documento) {
        service.actualizarDocTramite(id, documento);
        return ResponseEntity.ok("Documento actualizado con éxito");
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "consultarTareas/ventanilla/usuario/{usuario}/{activa}")
    public ResponseEntity<List<Tarea>> consultarTareasVentanilla(@PathVariable String usuario, @PathVariable boolean activa, @RequestParam int page, @RequestParam int size
            , @Valid Tarea filtros) {
        try {
            Map<String, Object> result = this.service.consultarTareasVentanilla(usuario, activa, page, size, filtros);
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.add("totalPages", result.get("totalPage").toString());
            responseHeader.add("rootSize", result.get("rootSize").toString());
            List<Tarea> data = (List<Tarea>) result.get("result");
            log.info("SUCESS");
            return new ResponseEntity<>(data, responseHeader, HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
