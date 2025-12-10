package org.origami.docs.service;

import org.origami.docs.entity.GCRecuperacion;
import org.origami.docs.entity.GCRetencion;
import org.origami.docs.repository.GCRecuperacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GCRecuperacionService {

    @Autowired
    private GCRecuperacionRepository repository;


    public List<GCRecuperacion> findAll() {
        return repository.findAll();
    }

    public GCRecuperacion guardar(GCRecuperacion recuperacion){
        return repository.save(recuperacion);
    }

    public void eliminar(GCRecuperacion recuperacion){
        repository.delete(recuperacion);
    }
}
