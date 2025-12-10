package org.sas.seguridad.service;

import org.sas.seguridad.entity.UsuarioVerificacion;
import org.sas.seguridad.repository.UsuarioVerificacionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioVerificacionService {

    @Autowired
    private UsuarioVerificacionRepo _verificacionRepo;

    public UsuarioVerificacion find(UsuarioVerificacion data) {
        return _verificacionRepo.findOne(Example.of(data)).orElse(null);
    }

    public UsuarioVerificacion save(UsuarioVerificacion data) {
        return _verificacionRepo.save(data);
    }

    public List<UsuarioVerificacion> findAll(UsuarioVerificacion data) {
        return _verificacionRepo.findAll(Example.of(data));
    }
 
}
