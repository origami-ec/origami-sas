package org.sas.seguridad.resource;

import org.sas.seguridad.entity.Rol;
import org.sas.seguridad.service.RolService;
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
public class RolResource {

    @Autowired
    private RolService service;

    @GetMapping("/roles/find")
    public ResponseEntity<?> findAll(@Valid Rol data, Pageable pageable) {
        try {
            Map<String, List> map = service.findAll(data, pageable);
            List<String> pages = map.get("pages");
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("totalPages", pages.get(0));
            responseHeaders.add("rootSize", pages.get(1));
            return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "rol/find", method = RequestMethod.POST)
    public ResponseEntity<?> consutarRol(@RequestBody Rol rol) {
        return new ResponseEntity<>(service.consutarRol(rol), HttpStatus.OK);
    }

    @RequestMapping(value = "rol/save", method = RequestMethod.POST)
    public ResponseEntity<?> guardarRol(@RequestBody Rol rol) {
        return new ResponseEntity<>(service.saveRol(rol), HttpStatus.OK);
    }

    @PostMapping(value = "rol/byUsuario/{idUsuario}")
    public ResponseEntity<?> getRolesByUsuario(@PathVariable Long idUsuario){
        return new ResponseEntity<>(service.getRolesByUsuario(idUsuario), HttpStatus.OK);
    }

}
