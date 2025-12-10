package org.sas.seguridad.resource;

import org.sas.seguridad.entity.CorreoFormato;
import org.sas.seguridad.entity.Valor;
import org.sas.seguridad.service.ValorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ValorResource {

    @Autowired
    private ValorService service;

    @PostMapping("/valor/find")
    public ResponseEntity<Valor> find(@RequestBody Valor data) {
        try {
            return new ResponseEntity<>(service.find(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/valor/findIn")
    public ResponseEntity<List<Valor>> find(@RequestBody List<Valor> dataIn) {
        try {
            return new ResponseEntity<>(service.findIn(dataIn), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/valor/findAll")
    public ResponseEntity<List<Valor>> findAll(@RequestBody Valor data) {
        try {
            return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/valor/save")
    public ResponseEntity<Valor> save(@RequestBody Valor data) {
        try {
            return new ResponseEntity<>(service.save(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/formatoNotificacion/find")
    public ResponseEntity<CorreoFormato> find(@Valid CorreoFormato data) {
        try {
            return new ResponseEntity<>(service.find(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/formatoNotificacion/findAll")
    public ResponseEntity<List<CorreoFormato>> findAll(@Valid CorreoFormato data) {
        try {
            return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/formatoNotificacion/save")
    public ResponseEntity<CorreoFormato> save(@RequestBody CorreoFormato data) {
        try {
            return new ResponseEntity<>(service.save(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
