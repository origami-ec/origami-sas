package org.origami.docs.service;

import org.origami.docs.entity.GCMedioAlmacenamiento;
import org.origami.docs.entity.GCRecuperacion;
import org.origami.docs.entity.GCTipoContenido;
import org.origami.docs.repository.GCMedioAlmacenamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GCMedioAlmacenamientoService {

    @Autowired
    private GCMedioAlmacenamientoRepository repository;

    public List<GCMedioAlmacenamiento> findAll() {
        return repository.findAll();
    }

    public GCMedioAlmacenamiento guardar(GCMedioAlmacenamiento medioAlmacenamiento){
        return repository.save(medioAlmacenamiento);
    }

    public void eliminar(GCMedioAlmacenamiento medioAlmacenamiento){
        repository.delete(medioAlmacenamiento);
    }
}
