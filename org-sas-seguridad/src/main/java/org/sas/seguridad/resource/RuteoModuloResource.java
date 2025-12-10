package org.sas.seguridad.resource;

import org.sas.seguridad.entity.RuteoModulo;
import org.sas.seguridad.service.RuteoModuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RuteoModuloResource {
    @Autowired
    private RuteoModuloService service;

    @PostMapping("/ruteo/findAll")
    public ResponseEntity<List<RuteoModulo>> findAll() {
        try {
            return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
