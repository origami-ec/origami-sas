package org.sas.seguridad.resource;

import org.sas.seguridad.entity.CorreoDominio;
import org.sas.seguridad.service.CorreoDominioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CorrreoDominioResource {

    @Autowired
    private CorreoDominioService dominioService;

    @PostMapping(value = "dominio/findAll")
    public ResponseEntity<?> findAll(@Valid CorreoDominio data) {
        return new ResponseEntity<>(dominioService.findAll(data), HttpStatus.OK);
    }
}
