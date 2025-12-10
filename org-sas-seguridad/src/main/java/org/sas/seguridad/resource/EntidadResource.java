package org.sas.seguridad.resource;

import org.sas.seguridad.entity.Entidad;
import org.sas.seguridad.service.EntidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class EntidadResource {

    @Autowired
    private EntidadService service;

    @PostMapping("/entidad/save")
    public ResponseEntity<?> save(@RequestBody Entidad data) {
        try {
            return new ResponseEntity<>(service.save(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/entidad/find")
    public ResponseEntity<?> find(@Valid Entidad data) {
        return new ResponseEntity<>(service.find(data), HttpStatus.OK);
    }

    @PostMapping("/entidad/findAll")
    public ResponseEntity<?> findAll(@Valid Entidad data) {
        return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
    }
}
