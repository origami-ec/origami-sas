package org.sas.seguridad.service;

import org.sas.seguridad.entity.RuteoModulo;
import org.sas.seguridad.repository.RuteoModuloRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuteoModuloService {

    @Autowired
    private RuteoModuloRepo ruteoModuloRepo;

    public List<RuteoModulo> findAll() {
        return ruteoModuloRepo.findAll();
    }
}
