package org.ibarra.resource;

import lombok.extern.slf4j.Slf4j;
import org.ibarra.dto.ParametrizacionProcesosDto;
import org.ibarra.service.ParametrizacionProcesosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/parametrizacionProcesos")
@Slf4j
public class ParametrizacionProcesosResource {
    @Autowired
    private ParametrizacionProcesosService service;


    @PostMapping(value = "save")
    public ResponseEntity<?> saveParametrizacion(@RequestBody ParametrizacionProcesosDto dto) {
        try {
            return new ResponseEntity<>(service.saveParametrizacion(dto), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "eliminar")
    public ResponseEntity<?> eliminarParametrizacion(@RequestBody ParametrizacionProcesosDto dto) {
        try {
            return new ResponseEntity<>(service.eliminarParametrizacion(dto), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "buscar/campos")
    public ResponseEntity<?> finfByCamposParametrizacionProcesos(@RequestBody ParametrizacionProcesosDto dto) {
        System.out.println("parametros {}" + dto.toString());

        return new ResponseEntity<>(service.findByCamposParametrizacionProcesos(dto), HttpStatus.OK);


    }
}