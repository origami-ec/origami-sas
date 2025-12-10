package org.sas.administrativo.resource;

import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.dto.commons.FirmaDocumentoDto;
import org.sas.administrativo.entity.FirmaDocumento;
import org.sas.administrativo.service.commons.FirmaDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FirmaDocumentoApi {
    @Autowired
    private FirmaDocumentoService service;

    @RequestMapping(value = "firmaDocumento/actualizarDesk", method = RequestMethod.POST)
    public ResponseEntity<?> actualizarDesk(@RequestBody FirmaDocumentoDto firmaDocumento) {
        return new ResponseEntity<>(service.actualizarFirmaDocumentosDesk(firmaDocumento), HttpStatus.OK);
    }

    @RequestMapping(value = "firmaDocumento/guardarInactivarFirmas", method = RequestMethod.POST)
    public ResponseEntity<?> actualizar(@RequestBody List<FirmaDocumento> list) {
        return new ResponseEntity<>(service.guardar(list), HttpStatus.OK);
    }

    @RequestMapping(value = "firmaDocumento/guardarInactivarFirma", method = RequestMethod.POST)
    public ResponseEntity<?> actualizar(@RequestBody FirmaDocumento firmaDocumento) {
        return new ResponseEntity<>(service.guardar(firmaDocumento), HttpStatus.OK);
    }

    @RequestMapping(value = "firmaDocumento/actualizar", method = RequestMethod.POST)
    public ResponseEntity<?> actualizar(@RequestBody FirmaDocumentoDto firmaDocumento) {
        return new ResponseEntity<>(service.actualizarFirmaDocumentos(firmaDocumento), HttpStatus.OK);
    }

    @RequestMapping(value = "firmaDocumento/actualizarPendiente", method = RequestMethod.POST)
    public ResponseEntity<?> actualizarPendientes(@RequestBody List<FirmaDocumentoDto> firmaDocumentos) {
        return new ResponseEntity<>(service.actualizarFirmaDocumentosPendientes(firmaDocumentos), HttpStatus.OK);
    }

    @RequestMapping(value = "firmaDocumento/verificarDocumentos", method = RequestMethod.POST)
    public ResponseEntity<?> verificarDocumentos(@RequestBody FirmaDocumentoDto firmaDocumento) {
        return new ResponseEntity<>(service.verificarDocumentos(firmaDocumento), HttpStatus.OK);
    }

    @RequestMapping(value = "firmaDocumento/consultarDocumentos", method = RequestMethod.POST)
    public ResponseEntity<?> consultarDocumentos(@RequestBody FirmaDocumentoDto firmaDocumento) {
        return new ResponseEntity<>(service.consultarDocumentos(firmaDocumento), HttpStatus.OK);
    }

    @RequestMapping(value = "firmaDocumento/consultarDocumentosXservidor/{servidor}", method = RequestMethod.POST)
    public ResponseEntity<?> consultarDocumentosXservidor(@PathVariable Long servidor) {
        return new ResponseEntity<>(service.consultarDocumentosXservidor(servidor), HttpStatus.OK);
    }

    @RequestMapping(value = "verificar/documento/firmado/tipo/{tipo}/referencia/{referencia}/estado/{estado}", method = RequestMethod.POST)
    public ResponseEntity<?> validarFirmaDocumento(@PathVariable String tipo, @PathVariable Long referencia, @PathVariable String estado) {
        return new ResponseEntity<>(service.verifcarDocumentoFirmado(tipo, referencia, estado), HttpStatus.OK);
    }

    @RequestMapping(value = "firmaDocumento/findByServidor", method = RequestMethod.POST)
    public ResponseEntity<?> findDocumentosByServidor(@RequestBody FirmaDocumento firmaDocumento) {
        return new ResponseEntity<>(service.findByServidorAndEstadoAnTipoFirma(firmaDocumento), HttpStatus.OK);
    }

    @PostMapping("/firmaDocumento/guardar")
    public ResponseEntity<RespuestaWs> register(@RequestBody FirmaDocumento data) {
        RespuestaWs resp = new RespuestaWs();
        try {
            resp = service.save(data);
        } catch (Exception e) {
            resp.setEstado(false);
            resp.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "firmaDocumento/pendiente/firma", method = RequestMethod.POST)
    public ResponseEntity<?> findDocumentosPendieteFirma(@RequestBody FirmaDocumento firmaDocumento) {
        return new ResponseEntity<>(service.findByEstadoAnTipoFirmaAndreferencia(firmaDocumento), HttpStatus.OK);
    }

    @PostMapping("/firmaDocumentoList/guardar")
    public ResponseEntity<?> guardarFirmasDocumentos(@RequestBody List<FirmaDocumento> data) {
        return new ResponseEntity<>(service.guardar(data), HttpStatus.OK);
    }

    @PostMapping("/firmaDocumentoList/notificacion/guardar")
    public ResponseEntity<?> guardarFirmaDocumentoNotificacion(@RequestBody List<FirmaDocumento> data) {
        return new ResponseEntity<>(service.guardarFirmaDocumentoNotificacion(data), HttpStatus.OK);
    }

    @RequestMapping(value = "firmaDocumento/findByServidorAndTramite", method = RequestMethod.POST)
    public ResponseEntity<?> findByServidorAndTramite(@RequestBody FirmaDocumento firmaDocumento) {
        return new ResponseEntity<>(service.findByServidorAndTramite(firmaDocumento), HttpStatus.OK);
    }

    @GetMapping("/firmaDocumento/countByServidor/{servidor}")
    public ResponseEntity<?> countByServidorEstado(@PathVariable Long servidor) {
        return new ResponseEntity<>(service.countByServidorEstado(servidor), HttpStatus.OK);
    }

    @PostMapping("/firmaDocumento/inactivar")
    public ResponseEntity<RespuestaWs> inactivarFirmaDocumento(@RequestBody List<FirmaDocumento> data) {
        return new ResponseEntity<>(service.inactivarFirmaDocumento(data), HttpStatus.OK);
    }

    @PostMapping("/firmaDocumento/eliminarDocumentos")
    public ResponseEntity<?> eliminarDocumentos(@RequestBody FirmaDocumento data) {
        service.inactivarFirmaDocumentos(data.getReferencia(), data.getTipo().getId(), data.getEstado().getId());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
