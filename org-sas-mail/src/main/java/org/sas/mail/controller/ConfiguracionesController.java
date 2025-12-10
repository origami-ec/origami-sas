package org.sas.mail.controller;

import jakarta.validation.Valid;
import org.sas.mail.entity.Correo;
import org.sas.mail.entity.CorreoSettings;
import org.sas.mail.services.CorreoReferenciaService;
import org.sas.mail.services.CorreoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class ConfiguracionesController {

    private Logger logger = Logger.getLogger(ConfiguracionesController.class.getName());

    @Autowired
    private CorreoService correoService;
    @Autowired
    private CorreoReferenciaService correoReferenciaService;

    @RequestMapping(value = "guardarConfiguraciones", method = RequestMethod.POST)
    public ResponseEntity<?> guardarConfiguraciones(@RequestBody CorreoSettings settings) {
        return new ResponseEntity<>(correoService.guardarConfiguraciones(settings), HttpStatus.OK);
    }



//    @PostMapping("correo/findAll")
    @RequestMapping(value = "correo/findAll", method = RequestMethod.GET)
    public ResponseEntity<?> search(@Valid Correo data, Pageable pageable) {
        Map<String, List> map;
        if(data.getDestinatario()!=null){
             map = correoService.findAll(data, pageable);
        }else{
            data.setEnviado(Boolean.TRUE);
            map = correoService.findAll(data,pageable);
        }

        List<String> pages = map.get("pages");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("totalPages", pages.get(0));
        responseHeaders.add("rootSize", pages.get(1));
        return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
    }

    @PostMapping("correo/reenviar")
    public String updateNotificacion(@RequestBody Correo data) {
        try {
            correoService.reenviarCorreo(data);
            return new String("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return new String("ERROR");
        }
    }
}
