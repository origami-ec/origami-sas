package org.ibarra.resource;

import org.ibarra.dto.Tarea;
import org.ibarra.service.TareaUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "process/")
public class TareaUsuarioResource {

    @Autowired
    private TareaUsuarioService tareaUsuarioService;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "mistareasactivas/usuario/{usuario}")
    public ResponseEntity<List<Tarea>> mistareasactivas(@PathVariable String usuario) {
        try {
            List<Tarea> result = tareaUsuarioService.listarTareasPorUsuarioActivas(usuario, 0, 0, null);
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.add("total", result.size() + "");
            return new ResponseEntity<>(result, responseHeader, HttpStatus.OK);
        } catch (Exception e) {
            //log.log(Level.SEVERE, "ERROR", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "mistareascompletadas/usuario/{usuario}")
    public ResponseEntity<List<Tarea>> mistareascompletadas(@PathVariable String usuario) {
        try {
            List<Tarea> result = tareaUsuarioService.listarTareasPorUsuarioCompletadas(usuario, 0, 0, null);
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.add("total", result.size() + "");
            return new ResponseEntity<>(result, responseHeader, HttpStatus.OK);
        } catch (Exception e) {
            //log.log(Level.SEVERE, "ERROR", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
