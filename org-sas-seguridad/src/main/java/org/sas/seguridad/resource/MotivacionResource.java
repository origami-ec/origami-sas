package org.sas.seguridad.resource;

import org.sas.seguridad.service.MotivacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MotivacionResource {

    @Autowired
    private MotivacionesService service;

    @RequestMapping(value = "motivacion", method = RequestMethod.GET)
    public ResponseEntity<?> consultarFraseMotivacion() {
        return new ResponseEntity<>(service.consultarFraseMotivacion(), HttpStatus.OK);
    }


}
