package org.origami.docs.resource;

import org.origami.docs.entity.Indexacion;
import org.origami.docs.entity.ListadoMaestroIndexacion;
import org.origami.docs.service.IndexacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class IndexacionResource {

    @Autowired
    private IndexacionService service;

    @RequestMapping(value = "indexaciones", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@Valid Indexacion data, Pageable pageable) {
        Map<String, List> map = service.find(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));

        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "indexacionesListadoMaestro", method = RequestMethod.GET)
    public ResponseEntity<?> indexacionesListadoMaestro(@Valid ListadoMaestroIndexacion data, Pageable pageable) {
        Map<String, List> map = service.find(data, pageable);
        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));

        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(path = "indexacion/validar", method = RequestMethod.POST)
    public ResponseEntity<?> consultar(@RequestBody Indexacion indexacion) {
        Indexacion response = service.validarIndexacionFormulario(indexacion);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "indexacionListadoMaestro/validar", method = RequestMethod.POST)
    public ResponseEntity<?> consultarIndexacionListadoMaestro(@RequestBody ListadoMaestroIndexacion indexacion) {
        ListadoMaestroIndexacion response = service.validarIndexacionFormulario(indexacion);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "indexacion/consultar", method = RequestMethod.GET)
    public ResponseEntity<?> consultar() {
        return new ResponseEntity<>(service.consultar(), HttpStatus.OK);
    }


    @RequestMapping(path = "indexacionListadoMaestro/consultar", method = RequestMethod.GET)
    public ResponseEntity<?> consultarindexacionListadoMaestro() {
        return new ResponseEntity<>(service.consultarindexacionListadoMaestro(), HttpStatus.OK);
    }


    @RequestMapping(path = "indexacion/consultar/{codigo}", method = RequestMethod.GET)
    public ResponseEntity<?> consultar(@PathVariable String codigo) {
        System.out.println("codigo>>"+codigo);
        return new ResponseEntity<>(service.consultar(codigo), HttpStatus.OK);
    }

    @RequestMapping(path = "indexacion/grabar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody Indexacion indexacion) {
        Indexacion response = service.guardar(indexacion);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "indexacionListadoMaestro/grabar", method = RequestMethod.POST)
    public ResponseEntity<?> guardarIndexacionListadoMaestro(@RequestBody ListadoMaestroIndexacion indexacion) {
        ListadoMaestroIndexacion response = service.guardar(indexacion);
        return new ResponseEntity<>(response, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }


}
