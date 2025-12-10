package org.ibarra.service;

import org.ibarra.dto.RespuestaWs;
import org.ibarra.entity.Tramite;
import org.ibarra.repository.TramiteRepo;
import org.ibarra.util.Utils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TramiteService {

    private static final Logger LOG = Logger.getLogger(TramiteService.class.getName());
    private final TramiteRepo repository;

    public TramiteService(TramiteRepo repository) {
        this.repository = repository;
    }

    public Page<Tramite> findAllPage(Tramite data, Pageable pageable) {
        try {
            if (data == null) {
                data = new Tramite();
            }
            return repository.findAll(Example.of(data), pageable);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }


    public RespuestaWs save(Tramite data) {
        RespuestaWs resp = new RespuestaWs();
        Tramite dataRes = this.repository.save(data);
        try {
            if(dataRes != null){
                resp.setEstado(true);
                resp.setMensaje("Datos procesados correctamente.");
                resp.setData(Utils.toJson(dataRes));
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            resp.setEstado(false);
            resp.setMensaje(e.getMessage());
        }
        return resp;
    }


}
