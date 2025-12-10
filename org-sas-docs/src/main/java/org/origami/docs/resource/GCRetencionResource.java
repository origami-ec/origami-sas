package org.origami.docs.resource;

import org.origami.docs.entity.GCRecuperacion;
import org.origami.docs.entity.GCRetencion;
import org.origami.docs.service.GCRetencionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GCRetencionResource {

    @Autowired
    private GCRetencionService service;


    @RequestMapping(value = "tiempoRetencion", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "tiempoRetencion/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody GCRetencion retencion) {
        return new ResponseEntity<>(service.guardar(retencion), HttpStatus.OK);
    }

    @RequestMapping(value = "tiempoRetencion/eliminar", method = RequestMethod.POST)
    public void eliminar(@RequestBody GCRetencion retencion) {
        service.eliminar(retencion);
    }
}
