package org.origami.docs.resource;

import org.origami.docs.entity.GCMedioAlmacenamiento;
import org.origami.docs.entity.GCRecuperacion;
import org.origami.docs.entity.GCTipoContenido;
import org.origami.docs.service.GCMedioAlmacenamientoService;
import org.origami.docs.service.GCTipoContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GCMedioAlmacenamientoResource {

    @Autowired
    private GCMedioAlmacenamientoService service;


    @RequestMapping(value = "medioAlmacenamientos", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "medioAlmacenamiento/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody GCMedioAlmacenamiento medioAlmacenamiento) {
        return new ResponseEntity<>(service.guardar(medioAlmacenamiento), HttpStatus.OK);
    }

    @RequestMapping(value = "medioAlmacenamiento/eliminar", method = RequestMethod.POST)
    public void eliminar(@RequestBody GCMedioAlmacenamiento medioAlmacenamiento) {
        service.eliminar(medioAlmacenamiento);
    }
}
