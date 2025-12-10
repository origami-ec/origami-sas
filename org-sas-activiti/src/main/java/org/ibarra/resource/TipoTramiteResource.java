package org.ibarra.resource;

import lombok.extern.slf4j.Slf4j;
import org.ibarra.dto.TipoTramiteDto;
import org.ibarra.dto.TipoTramiteRequisitoDto;
import org.ibarra.service.TipoTramiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TipoTramiteResource {
    @Autowired
    private TipoTramiteService service;

    @PostMapping("tipoTramite/consultar")
    public ResponseEntity<?> guardar(@RequestBody TipoTramiteDto dto) {
        return new ResponseEntity<>(service.consultar(dto), HttpStatus.OK);
    }


    @PostMapping("listar/tipoTramite/{activiyKeys}")
    public ResponseEntity<?> listarTiposTramite(@PathVariable String activiyKeys) {
        System.out.println("Datos " + activiyKeys);
        return new ResponseEntity<>(service.findByTiposTramiteActivitiKey(activiyKeys), HttpStatus.OK);
    }

    @PostMapping("listar/tipoTramites")
    public ResponseEntity<?> listarTiposTramiteTodos() {
        return new ResponseEntity<>(service.findAllByTipoTramites(), HttpStatus.OK);
    }

    @PostMapping("guardar/tipoTramite")
    public ResponseEntity<?> guardarTipoTramite(@RequestBody TipoTramiteDto tipoTramiteDto) {
        return new ResponseEntity<>(service.guardarTipoTramite(tipoTramiteDto), HttpStatus.OK);
    }

    @PostMapping("guardar/tipoTramite/requisito")
    public ResponseEntity<?> guardarTipoTramiteRequisito(@RequestBody TipoTramiteRequisitoDto tipoTramiteRequisitoDto) {
        return new ResponseEntity<>(service.guardarTipoTramiteRequisito(tipoTramiteRequisitoDto), HttpStatus.OK);
    }
}
