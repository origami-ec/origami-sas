package org.origami.docs.service;

import org.origami.docs.entity.HistoricoOrigamigt;
import org.origami.docs.repository.HistoricoOrigamigtRepo;
import org.origami.docs.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HistoricoOrigamigtService {
    @Autowired
    private HistoricoOrigamigtRepo repository;

    public List<HistoricoOrigamigt> consultarHistorico(HistoricoOrigamigt data) {
        return repository.findAll(Example.of(data));
    }

    public HistoricoOrigamigt guardarHistoricoOrigami(HistoricoOrigamigt data) {
        HistoricoOrigamigt ht;
        try {
            data.setFechaRegistro(Utils.getFechaMongo());
            ht = repository.save(data);
        } catch (Exception e) {
            ht = null;
        }
        return ht;
    }
}
