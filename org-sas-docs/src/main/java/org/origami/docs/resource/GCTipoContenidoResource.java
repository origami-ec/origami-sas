package org.origami.docs.resource;

import org.origami.docs.entity.GCRecuperacion;
import org.origami.docs.entity.GCTipoContenido;
import org.origami.docs.service.GCTipoContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GCTipoContenidoResource {

    @Autowired
    private GCTipoContenidoService service;


    @RequestMapping(value = "tipoContenidos", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "tipoContenidos/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody GCTipoContenido tipoContenido) {
        return new ResponseEntity<>(service.guardar(tipoContenido), HttpStatus.OK);
    }

    @RequestMapping(value = "tipoContenido/eliminar", method = RequestMethod.POST)
    public void eliminar(@RequestBody GCTipoContenido tipoContenido) {
        service.eliminar(tipoContenido);
    }

}
