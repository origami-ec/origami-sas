package org.ibarra.resource;

import jakarta.validation.Valid;
import org.ibarra.dto.Tarea;
import org.ibarra.service.BandejaTareaService;
import org.ibarra.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Controller
@RequestMapping(value = "process/")
public class BandejaTareaResource {
    @Autowired
    private BandejaTareaService bandejaTareaService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "consultarTareas/usuario/{usuario}/{activa}")
    public ResponseEntity<List<Tarea>> consultarTareas(@PathVariable String usuario, @PathVariable boolean activa, @RequestParam int page, @RequestParam int size
            , @Valid Tarea filtros) {
        try {
            Map<String, Object> result = bandejaTareaService.consultarTareas(usuario, activa, page, size, filtros);
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.add("totalPages", result.get("totalPage").toString());
            responseHeader.add("rootSize", result.get("rootSize").toString());
            List<Tarea> data = (List<Tarea>) result.get("result");

            return new ResponseEntity<>(data, responseHeader, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
