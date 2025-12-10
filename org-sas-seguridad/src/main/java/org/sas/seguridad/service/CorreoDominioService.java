package org.sas.seguridad.service;

import org.sas.seguridad.entity.CorreoDominio;
import org.sas.seguridad.repository.CorreoDominioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CorreoDominioService {
    @Autowired
    private CorreoDominioRepo dominioRepository;

    public CorreoDominio find(CorreoDominio data) {
        Optional<CorreoDominio> dataDB = dominioRepository.findOne(Example.of(data));
        return dataDB.orElse(null);
    }

    public List<CorreoDominio> findAll(CorreoDominio data) {
        return dominioRepository.findAll(Example.of(data), Sort.by(Sort.Direction.ASC, "id"));
    }
}
