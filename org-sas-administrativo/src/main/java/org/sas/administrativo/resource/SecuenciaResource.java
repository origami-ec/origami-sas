package org.sas.administrativo.resource;


import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.entity.configuracion.SecuenciaGeneral;
import org.sas.administrativo.service.commons.SecuenciaGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "configuraciones/secuencia/")
public class SecuenciaResource {
    @Autowired
    private SecuenciaGeneralService service;

    @PostMapping(value = "generarSecuencia")
    public ResponseEntity<?> generarSecuencia(@RequestBody RespuestaWs respuestaWs) {
        try {
            return new ResponseEntity<>(service.generarSecuencia(respuestaWs), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "generarSecuencia/code/{code}/periodo/{periodo}")
    public ResponseEntity<?> generarSecuenciaTipoAndAnio(@PathVariable String code, @PathVariable Integer periodo) {
        try {
            return new ResponseEntity<>(service.getSecuencia(code, periodo), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "generar/codigo/{code}/periodo/{periodo}")
    public ResponseEntity<?> generarCodigoSecuencia(@PathVariable String code, @PathVariable Integer periodo) {
        try {
            return new ResponseEntity<>(service.getSecuenciaGeneralRecaudacion(code, periodo), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "generarSecuencia/code/{code}")
    public ResponseEntity<?> generarSecuenciaBycode(@PathVariable String code) {
        try {
            return new ResponseEntity<>(service.getSecuenciaByCode(code), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "find/code/{code}")
    public ResponseEntity<?> findSecuenciaByCode(@PathVariable String code) {
        try {
            return new ResponseEntity<>(service.findSecuenciaByCode(code), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "generarSecuencia/update/code/{code}")
    public ResponseEntity<?> getUpdateSecuenciaByCode(@PathVariable String code) {
        try {
            return new ResponseEntity<>(service.getUpdateSecuenciaByCode(code), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("save")
    public ResponseEntity<SecuenciaGeneral> save(@RequestBody SecuenciaGeneral data) {
        try {
            return new ResponseEntity<>(service.save(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("findSecuencia/code/{code}/anio/{anio}")
    public ResponseEntity<?> findSecuencia(@PathVariable String code, @PathVariable Integer anio) {
        try {
            return new ResponseEntity<>(service.getSecuenciaByCode(code, anio), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
