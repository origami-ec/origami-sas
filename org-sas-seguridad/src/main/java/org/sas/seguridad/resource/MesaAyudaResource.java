package org.sas.seguridad.resource;

import org.sas.seguridad.dto.MesaAyudaDto;
import org.sas.seguridad.dto.RespuestaWs;
import org.sas.seguridad.service.MesaAyudaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping({"/mesaAyuda"})
public class MesaAyudaResource {
    @Autowired
    private MesaAyudaService service;
    private static final Logger LOG = Logger.getLogger(MesaAyudaResource.class.getName());

    public MesaAyudaResource() {
    }

    @RequestMapping(
            value = {"/guardar"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity<?> guardar(@RequestBody MesaAyudaDto mesaAyuda) {
        try {
            return new ResponseEntity(this.service.save(mesaAyuda), HttpStatus.OK);
        } catch (Exception var3) {
            var3.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping({"/maxMesaAyuda"})
    public ResponseEntity<?> getMaxOrdenMesaAyuda() {
        RespuestaWs resp = new RespuestaWs();

        try {
            resp = this.service.getMaxMesaAyuda();
        } catch (Exception var3) {
            LOG.log(Level.SEVERE, "", var3);
            resp.setEstado(false);
            resp.setMensaje(var3.getMessage());
        }

        return new ResponseEntity(resp, HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/findAll"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity<?> findAllMesaAyuda() {
        return new ResponseEntity(this.service.findAllMesaAyuda(), HttpStatus.OK);
    }

    @RequestMapping(
            value = {"findById/{id}"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity<?> findMesaAyuda(@PathVariable Long id) {
        return new ResponseEntity(this.service.findMesaAyuda(id), HttpStatus.OK);
    }
}