package org.ibarra.resource;

import jakarta.validation.Valid;
import org.ibarra.dto.RespuestaWs;
import org.ibarra.dto.TareaPendiente;
import org.ibarra.entity.Tramite;
import org.ibarra.service.HistoricoTramiteService;
import org.ibarra.service.TramiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController()
@RequestMapping("/tramite")

public class TramiteResource {
    private static final Logger LOG = Logger.getLogger(TramiteResource.class.getName());
    private final TramiteService catalogoMefService;

    @Autowired
    HistoricoTramiteService service;

    public TramiteResource(TramiteService catalogoMefService) {
        this.catalogoMefService = catalogoMefService;
    }

    @GetMapping("/findAll/page")
    public ResponseEntity<List<Tramite>> findAllPage(@Valid Tramite data, Pageable pageable) {
        Page<Tramite> allPage = null;
        try {
            allPage = catalogoMefService.findAllPage(data, pageable);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("rootSize", allPage.getTotalElements() + "");
            headers.add("totalPage", allPage.getTotalPages() + "");
            return new ResponseEntity<>(allPage.getContent(), headers, HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<RespuestaWs> register(@RequestBody Tramite data) {
        RespuestaWs resp = new RespuestaWs();
        try {
            resp = catalogoMefService.save(data);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            resp.setEstado(false);
            resp.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/findAllTareasActivas/{usuario}")
    public ResponseEntity<List<TareaPendiente>> findAllTareasActivas(@PathVariable String usuario, Pageable pageable) {
        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            List<TareaPendiente> tareas = service.listarTareasActivas(usuario, pageable, headers);
            if (tareas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return new ResponseEntity<>(tareas, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
