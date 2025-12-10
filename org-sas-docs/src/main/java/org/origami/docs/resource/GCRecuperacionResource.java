package org.origami.docs.resource;

import org.origami.docs.entity.GCRecuperacion;
import org.origami.docs.service.GCRecuperacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GCRecuperacionResource {

    @Autowired
    private GCRecuperacionService service;

    @RequestMapping(value = "recuperacion", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "recuperacion/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody GCRecuperacion recuperacion) {
        return new ResponseEntity<>(service.guardar(recuperacion), HttpStatus.OK);
    }

    @RequestMapping(value = "recuperacion/eliminar", method = RequestMethod.POST)
    public void eliminar(@RequestBody GCRecuperacion recuperacion) {
        service.eliminar(recuperacion);
    }
}
