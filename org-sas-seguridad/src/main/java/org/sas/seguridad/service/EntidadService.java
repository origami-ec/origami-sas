package org.sas.seguridad.service;

import org.sas.seguridad.entity.Entidad;
import org.sas.seguridad.repository.EntidadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntidadService {

    @Autowired
    private EntidadRepo _EntidadRepo;

    public Entidad find(Entidad data) {
        return _EntidadRepo.findOne(Example.of(data)).orElse(null);
    }

    public Entidad save(Entidad data) {
        return _EntidadRepo.save(data);
    }

    public List<Entidad> findAll(Entidad data) {
        return _EntidadRepo.findAll(Example.of(data));
    }
}
