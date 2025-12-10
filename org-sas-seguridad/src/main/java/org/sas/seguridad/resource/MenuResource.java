package org.sas.seguridad.resource;

import org.sas.seguridad.dto.RespuestaWs;
import org.sas.seguridad.entity.Menu;
import org.sas.seguridad.entity.MenuRol;
import org.sas.seguridad.entity.MenuTipo;
import org.sas.seguridad.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MenuResource {

    @Autowired
    private MenuService service;

    @PostMapping("/menu/find")
    public ResponseEntity<Menu> find(@Valid Menu data) {
        try {
            return new ResponseEntity<>(service.find(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/redis/reiniciarValores")
    public ResponseEntity<RespuestaWs> reiniciarValores() {
        try {
            return new ResponseEntity<>(service.reiniciarValores(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/menu/findAll")
    public ResponseEntity<List<Menu>> findAll(@Valid Menu data) {
        try {
            return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/menu/findAll/page")
    public ResponseEntity<List<Menu>> findAll(@Valid Menu menu, Pageable pageable) {
        try {
            return new ResponseEntity<>(service.findAll(menu, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/menuBar/find")
    public ResponseEntity<MenuTipo> find(@Valid MenuTipo menuBar) {
        try {
            return new ResponseEntity<>(service.find(menuBar), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/menusTipo/findAll")
    public ResponseEntity<List<MenuTipo>> findAll(@Valid MenuTipo menuTipo) {
        try {
            return new ResponseEntity<>(service.findAllMenuTipo(menuTipo), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/menusBar/find/{user}")
    public ResponseEntity<List<MenuTipo>> findAll(@PathVariable String user) {
        try {
            return new ResponseEntity<>(service.findAll(user), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/menusRol/findAll")
    public ResponseEntity<List<MenuRol>> findAllMenuRol(@Valid MenuRol data) {
        try {
            return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/menu/guardar")
    public ResponseEntity<Menu> registrar(@RequestBody Menu data) {
        try {
            return new ResponseEntity<>(service.save(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/menu/eliminar")
    public ResponseEntity<?> eliminarMenu(@RequestBody Menu data) {
        try {
            service.delete(data);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping(value = "/menuRol/guardar")
    public ResponseEntity<?> guardarRol(@RequestBody MenuRol data) {
        try {
            return new ResponseEntity<>(service.save(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "menusRol/delete")
    public ResponseEntity<?> deleteMenuRol(@RequestBody MenuRol data) {
        try {
            System.out.println("data menu rol: " + data.toString());
            Boolean d = service.delete(data);
            System.out.println("del: "  + d);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }


    @PostMapping(value = "menus/find")
    public ResponseEntity<List<Menu>> findAllMenu(@Valid Menu data, Pageable pageable) {
        return new ResponseEntity<>(service.findAll(data, pageable), HttpStatus.OK);
    }


}
