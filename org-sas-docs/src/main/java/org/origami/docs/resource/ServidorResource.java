package org.origami.docs.resource;

import org.origami.docs.entity.origamigt.talentoHumano.Servidor;
import org.origami.docs.service.ServidorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServidorResource {
    @Autowired
    private ServidorServices services;

    @PostMapping(value = "/servidor/cargo/guardar")
    public ResponseEntity<?> guardarServidorCargo(@RequestBody Servidor data) {
        System.out.println("guardarServidorCargo");
        return new ResponseEntity<>(services.guardar(data), HttpStatus.OK);
    }
}
