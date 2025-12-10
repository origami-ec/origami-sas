package org.sas.seguridad.service;

import org.sas.seguridad.entity.Rol;
import org.sas.seguridad.repository.RolRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RolService {

    @Autowired
    private RolRepo rolRepo;

    public List<Rol> findAll(Rol rol) {
        return rolRepo.findAll(Example.of(rol));
    }

    public Map<String, List> findAll(Rol rol, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();
        Page<Rol> result = rolRepo.findAll(Example.of(rol, exampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(rolRepo.count(Example.of(rol, exampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public Rol consutarRol(Rol rol) {
        return rolRepo.findOne(Example.of(rol)).orElse(null);
    }

    public Rol saveRol(Rol rol) {
        return rolRepo.save(rol);
    }

    public List<Rol> getRolesByUsuario (Long idUsuario) {
        try {
            return rolRepo.getRolesByUsuario(idUsuario);
        }catch (Exception e) {
            return null;
        }
    }
}
