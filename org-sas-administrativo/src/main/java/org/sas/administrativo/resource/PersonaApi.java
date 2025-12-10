package org.sas.administrativo.resource;

import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.dto.commons.PersonaDto;
import org.sas.administrativo.entity.Persona;
import org.sas.administrativo.service.commons.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/cliente")
public class PersonaApi {

    private static final Logger LOG = Logger.getLogger(PersonaApi.class.getName());
    @Autowired
    private PersonaService service;

    @PostMapping("/consultarSolicitante/{id}")
    public ResponseEntity<?> consultarSolicitante(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.consultarSolicitante(id), HttpStatus.OK);
        } catch (Exception e) {
            RespuestaWs rw = new RespuestaWs(false, null);
            rw.setMensaje(e.getMessage());
            return new ResponseEntity<>(rw, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/consultaByDinardapSinGuardar")
    public ResponseEntity<?> consultaByDinardapSinGuardar(@RequestBody Persona data) {
        try {
            LOG.info("consultar metodo  " + data.getNumIdentificacion());
            return new ResponseEntity<>(service.consultaByDinardapSinGuardar(data), HttpStatus.OK);
        } catch (Exception e) {
            RespuestaWs rw = new RespuestaWs(false, null);
            rw.setMensaje(e.getMessage());
            return new ResponseEntity<>(rw, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/consultar")
    public ResponseEntity<?> consultar(@RequestBody Persona data) {
        try {
            LOG.info("consultar metodo  " + data.getNumIdentificacion());
            return new ResponseEntity<>(service.consultar(data), HttpStatus.OK);
        } catch (Exception e) {
            RespuestaWs rw = new RespuestaWs(false, null);
            rw.setMensaje(e.getMessage());
            return new ResponseEntity<>(rw, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/consultarDatosPersona")
    public ResponseEntity<?> consultarDatosPersona(@RequestBody RespuestaWs data) {
        try {
            return new ResponseEntity<>(service.consultarDatosPersona(data), HttpStatus.OK);
        } catch (Exception e) {
            RespuestaWs rw = new RespuestaWs(false, null);
            rw.setMensaje(e.getMessage());
            return new ResponseEntity<>(rw, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardar(@RequestBody PersonaDto data) {
        try {
            return new ResponseEntity<>(service.guardarPersona(data), HttpStatus.OK);
        } catch (Exception e) {
            RespuestaWs rw = new RespuestaWs(false, null);
            rw.setMensaje(e.getMessage());
            return new ResponseEntity<>(rw, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
