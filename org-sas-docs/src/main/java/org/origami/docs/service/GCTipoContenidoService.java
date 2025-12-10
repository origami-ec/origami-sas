package org.origami.docs.service;

import org.origami.docs.entity.Color;
import org.origami.docs.entity.GCRecuperacion;
import org.origami.docs.entity.GCTipoContenido;
import org.origami.docs.repository.GCTipoContenidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GCTipoContenidoService {

    @Autowired
    private GCTipoContenidoRepository repository;


    public List<GCTipoContenido> findAll() {
        return repository.findAll();
    }

    public GCTipoContenido guardar(GCTipoContenido tipoContenido){
        return repository.save(tipoContenido);
    }

    public void eliminar(GCTipoContenido tipoContenido){
        repository.delete(tipoContenido);
    }
}
