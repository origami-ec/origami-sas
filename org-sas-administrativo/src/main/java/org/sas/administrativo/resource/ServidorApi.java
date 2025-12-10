package org.sas.administrativo.resource;

import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.entity.talentohumano.Servidor;
import org.sas.administrativo.service.talentohumano.ServidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/servidor")
public class ServidorApi {
    @Autowired
    private ServidorService service;

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardarServidor(@RequestBody Servidor data) {
        try {
            return new ResponseEntity<>(service.guardar(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/informacionXpersona")
    public ResponseEntity<?> getDatoServidorXpersona(@RequestBody RespuestaWs data) {
        return new ResponseEntity<>(service.datoServidorXpersona(data), HttpStatus.OK);
    }

    @PostMapping("/servidorDatos/find")
    public ResponseEntity<?> getServidorDatos(@RequestBody Servidor data) {
        return new ResponseEntity<>(service.findServidorDatos(data), HttpStatus.OK);
    }

    @PostMapping("/user/{user}")
    public ResponseEntity<?> getServidorByUser(@PathVariable String user) {
        return new ResponseEntity<>(service.getServidorByUser(user), HttpStatus.OK);
    }
}
