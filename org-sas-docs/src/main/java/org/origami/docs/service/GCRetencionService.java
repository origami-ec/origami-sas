package org.origami.docs.service;

import org.origami.docs.entity.GCRecuperacion;
import org.origami.docs.entity.GCRetencion;
import org.origami.docs.entity.GCTipoContenido;
import org.origami.docs.repository.GCRetencionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GCRetencionService {

    @Autowired
    private GCRetencionRepository repository;

    public List<GCRetencion> findAll() {
        return repository.findAll();
    }

    public GCRetencion guardar(GCRetencion retencion){
        return repository.save(retencion);
    }

    public void eliminar(GCRetencion retencion){
        repository.delete(retencion);
    }
}
