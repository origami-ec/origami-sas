package org.sas.administrativo.resource;


import org.sas.administrativo.entity.talentohumano.UnidadAdministrativa;
import org.sas.administrativo.service.talentohumano.UnidadAdministrativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidadAdministrativa")
public class UnidadAdministrativaApi {

    @Autowired
    private UnidadAdministrativaService service;

    @PostMapping(value = "/findAll")
    public ResponseEntity<?> consutarUnidadesAdministrativas() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    /*
    Este servicio web devuelve un dto de departamentos
     */
    @PostMapping(value = "/consultaDepartamentos")
    public ResponseEntity<?> consultaDepartamento() {
        return new ResponseEntity<>(service.consultarDepartamentos(), HttpStatus.OK);
    }

    @PostMapping(value = "/padre/estado/{estado}")
    public ResponseEntity<?> getUnidadAdministrativaPadre(@PathVariable String estado) {
        return new ResponseEntity<>(service.getAllPadre(estado), HttpStatus.OK);
    }

    @PostMapping(value = "/findAll/datos")
    public ResponseEntity<?> getUnidadesDatos(@RequestBody UnidadAdministrativa data) {
        try {
            return new ResponseEntity<>(service.getUnidadesDatos(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/find")
    public ResponseEntity<?> getUnidadByPadre(@RequestBody UnidadAdministrativa data) {
        try {
            return new ResponseEntity<>(service.getUnidadAdministrativa(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/consultar")
    public ResponseEntity<?> consultarUnidadAdministrativa(@RequestBody UnidadAdministrativa data) {
        try {
            return new ResponseEntity<>(service.consultarUnidadAdministrativa(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardarUnidad(@RequestBody UnidadAdministrativa data) {
        return new ResponseEntity<>(service.guardar(data), HttpStatus.OK);
    }


    @PostMapping("/unidadesDto")
    public ResponseEntity<?> getUnidadesDto() {
        try {
            return new ResponseEntity<>(service.findAllUnidadDto(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getUnidadById/{id}")
    public ResponseEntity<?> getUnidadAdministrativaById(@PathVariable Long id) {
        return new ResponseEntity<>(service.getUnidadAdministrativaById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/consultarDirecciones")
    public ResponseEntity<?> consultarDirecciones() {
        return new ResponseEntity<>(service.findAllDirecciones(), HttpStatus.OK);
    }

    @PostMapping(value = "/consultarDirecionesYOtras")
    public ResponseEntity<?> consultarDirecionesYOtras() {
        return new ResponseEntity<>(service.fillAllDireccionesCompletas(), HttpStatus.OK);
    }


    @PostMapping(value = "/hijas")
    public ResponseEntity<?> getServidoresDireccion2(@RequestParam Long padre) {
        return new ResponseEntity<>(service.buscarUnidadesHijas(padre), HttpStatus.OK);
    }


    @PostMapping(value = "/listarUnidadesAdministrativasPorPadre")
    public ResponseEntity<?> listarUnidadesAdministrativasPorPadre(@RequestBody List<UnidadAdministrativa> data) {
        return new ResponseEntity<>(service.listarUnidadesAdministrativasPorPadre(data), HttpStatus.OK);
    }

    @PostMapping("/buscarUnidad/servidor/{idservidor}")
    public ResponseEntity<?> buscarUnidadAdministrativo(@PathVariable Long idservidor) {
        return new ResponseEntity<>(service.buscarUnidadAdministrativa(idservidor), HttpStatus.OK);
    }

    @GetMapping(value = "/direccion/by/{idUnidad}")
    public ResponseEntity<?> obtenerDireccionByIdUnidad(@PathVariable Long idUnidad) {
        return new ResponseEntity<>(service.obtenerDireccionByIdUnidad(idUnidad), HttpStatus.OK);
    }

    @PostMapping("/unidades")
    public ResponseEntity<?> getUnidades() {
        try {
            return new ResponseEntity<>(service.findAllUnidades(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
