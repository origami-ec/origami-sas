package org.ibarra.resource;

import jakarta.servlet.http.HttpServletResponse;
import org.activiti.bpmn.model.BpmnModel;
import org.ibarra.dto.*;
import org.ibarra.dto.flujoDinamico.Servicio;
import org.ibarra.service.EdicionDiagramaService;
import org.ibarra.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Controller
@CrossOrigin(value = "*")
@RequestMapping(value = "bpmn/")
public class EdicionDiagramaResources {

    @Autowired
    private EdicionDiagramaService diagramaService;
    @Autowired
    private ProcessService processService;

    @PostMapping(path = "/model/tipoTramite/{id}")
    public ResponseEntity<BpmnModel> getBpmnDiagram(@PathVariable Long id) {
        TipoTramiteDto tramiteDto = new TipoTramiteDto();
        tramiteDto.setId(id);
        return ResponseEntity.ok(diagramaService.loadBpmnDiagram(tramiteDto));
    }

    @PostMapping(path = "/xml/tipoTramite/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE}, consumes = {MediaType.ALL_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> loadFile(@PathVariable Long id) {
        TipoTramiteDto tramiteDto = new TipoTramiteDto();
        tramiteDto.setId(id);
        return ResponseEntity.ok(diagramaService.loadBpmnDiagramXml(tramiteDto));
    }

    @PostMapping(path = "/xml/timer/tipoTramite/{id}", produces = {MediaType.APPLICATION_XML_VALUE + ";charset=UTF-8", MediaType.TEXT_XML_VALUE + ";charset=UTF-8"})
    @ResponseBody
    public ResponseEntity<byte[]> loadFileAddTimer(@PathVariable Long id) {
        TipoTramiteDto tramiteDto = new TipoTramiteDto();
        tramiteDto.setId(id);
        return ResponseEntity.ok(diagramaService.loadBpmnDiagramXmlTimer(tramiteDto));
    }

    @PostMapping(path = "/data/tipoTramite/{id}")
    public ResponseEntity<BpmnXml> buscarBpmnModel(@PathVariable Long id) {
        TipoTramiteDto tramiteDto = new TipoTramiteDto();
        tramiteDto.setId(id);
        return ResponseEntity.ok(diagramaService.buscarBpmnXml(tramiteDto));
    }

    @PostMapping(path = "/actualizar/xml/", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespuestaWs> guardarXml(@RequestBody BpmnXml bpmnXml) {
        RespuestaWs respuestaWs = diagramaService.guardarXml(bpmnXml);
        if (respuestaWs != null) {
            if (Boolean.TRUE.equals(respuestaWs.getEstado())) {
                if (Boolean.TRUE.equals(bpmnXml.getActualizarProceso())) {
                    this.processService.processImplement(bpmnXml.getTipoTramite());
                }
            }
        }
        return ResponseEntity.ok(respuestaWs);
    }


    @PostMapping(path = "/xml/timer/ruta/{rutaBase64}/pattern/{periodo}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespuestaWs> agregarTimerArchivo(@PathVariable String rutaBase64, @PathVariable String periodo) {
        return ResponseEntity.ok(diagramaService.agregarTimerPorDefecto(rutaBase64, periodo));
    }

    @GetMapping(path = "/duracion/{pattern}")
    public ResponseEntity<String> getDurationExample(@PathVariable String pattern) {
        System.out.println("PT10M | P1DT1H10M10.5S");
        Duration fromChar2 = Duration.parse(pattern);
        return ResponseEntity.ok(fromChar2.toSeconds() + "");
    }

    @PostMapping(path = "/xml/tramite/{idTramite}", produces = {MediaType.APPLICATION_XML_VALUE + ";charset=UTF-8", MediaType.TEXT_XML_VALUE + ";charset=UTF-8"}, consumes = {MediaType.ALL_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> loadBpmnXmnlTramite(@PathVariable Long idTramite) {
        return ResponseEntity.ok(diagramaService.loadBpmnDiagramXmlTramite(idTramite));
    }

    @PostMapping(path = "/consulta/tramite/{idTramite}", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
    public ResponseEntity<HistoricoTramiteDto> guardarXml(@PathVariable Long idTramite) {
        HistoricoTramiteDto respuestaWs = diagramaService.buscarTramite(idTramite, null);
        return ResponseEntity.ok(respuestaWs);
    }
    @PostMapping(path = "/consulta/numtramite/{numtramite}", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
    public ResponseEntity<HistoricoTramiteDto> numtramite(@PathVariable String numtramite) {
        HistoricoTramiteDto respuestaWs = diagramaService.buscarXnumtramite(numtramite);
        return ResponseEntity.ok(respuestaWs);
    }

    @PostMapping(path = "/tarea/actualizar", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
    public ResponseEntity<RespuestaWs> redeployTramite(@RequestBody TaskModelTramite taskModelTramite) {
        RespuestaWs respuestaWs = diagramaService.actualizarTarea(taskModelTramite);
        return ResponseEntity.ok(respuestaWs);
    }

    @PostMapping(path = "/bpmn/svg/pdf/{tipo}", produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
    public void getPdf(@RequestBody HistoricoTramiteDto tramite, @PathVariable String tipo, HttpServletResponse response) {
        diagramaService.generarDiagrama(tramite, tipo, response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @PostMapping(path = "/servicio/to/xml", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<BpmnXml> buscarBpmnModel(@RequestBody Servicio servicio) {
        return ResponseEntity.ok(diagramaService.generarBpmn(servicio));
    }

}
