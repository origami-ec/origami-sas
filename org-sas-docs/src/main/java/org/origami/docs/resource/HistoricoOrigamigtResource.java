package org.origami.docs.resource;

import org.origami.docs.entity.HistoricoOrigamigt;
import org.origami.docs.service.HistoricoOrigamigtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistoricoOrigamigtResource {
    @Autowired
    private HistoricoOrigamigtService service;

    @PostMapping(value = "/guardar/historico/origamigt")
    public ResponseEntity<?> guardarHistorico(@RequestBody HistoricoOrigamigt data) {
        return new ResponseEntity<>(service.guardarHistoricoOrigami(data), HttpStatus.OK);
    }

    @PostMapping(value = "/consultarHistorico")
    public ResponseEntity<?> consultarHistorico(@RequestBody HistoricoOrigamigt data) {
        return new ResponseEntity<>(service.consultarHistorico(data), HttpStatus.OK);
    }
}
