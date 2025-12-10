package org.sas.seguridad.resource;

import org.sas.seguridad.entity.UsuarioRol;
import org.sas.seguridad.service.UsuarioRolService;
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
public class UsuarioRolResource {

    @Autowired
    private UsuarioRolService service;

    @PostMapping(value = "/usuarioRol/find")
    public ResponseEntity<?> find(@Valid UsuarioRol data) {
        try {
            return new ResponseEntity<>(service.find(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "consultar/usuarioXrol/{rol}")
    public ResponseEntity<?> consultarUsuarioXrol(@PathVariable String rol) {
        try {
            return new ResponseEntity<>(service.consultarUsuarioXrol(rol), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "consultar/usuario/{usuario}")
    public ResponseEntity<?> consultarUsuario(@PathVariable String usuario) {
        try {
            return new ResponseEntity<>(service.consultarUsuario(usuario), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/usuarioRol/findAll")
    public ResponseEntity<?> findAll(@Valid UsuarioRol data) {
        try {
            System.out.println("data " +data);
            return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/usuarioRol/saveAll")
    public ResponseEntity<List<UsuarioRol>> saveAll(@RequestBody List<UsuarioRol> rolUsuario) {
        try {
            return new ResponseEntity<>(service.saveAll(rolUsuario), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/usuarioRol/deleteAll")
    public ResponseEntity<?> deleteAll(@RequestBody List<UsuarioRol> rolUsuarios) {
        try {
            service.deleteAll(rolUsuarios);
            return new ResponseEntity<>(rolUsuarios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/usuarioRol/save")
    public ResponseEntity<?> save(@RequestBody UsuarioRol data) {
        try {
            return new ResponseEntity<>(service.save(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


    @PostMapping(value = "/usuarioRol/findAll/page")
    public ResponseEntity<?> findAll(@Valid UsuarioRol data, Pageable pageable) {
        try {
            Map<String, List> map = service.findAll(data, pageable);
            List<String> pages = map.get("pages");
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("totalPages", pages.get(0));
            responseHeaders.add("rootSize", pages.get(1));
            return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/usuarioRol/delete")
    public ResponseEntity<?> delete(@RequestBody UsuarioRol data) {
        try {
            service.delete(data);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }


}
