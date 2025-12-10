package org.sas.seguridad.service;

import org.sas.seguridad.entity.CorreoFormato;
import org.sas.seguridad.entity.Valor;
import org.sas.seguridad.repository.CorreoFormatoRepo;
import org.sas.seguridad.repository.ValorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValorService {

    @Autowired
    private ValorRepo _ValorRepo;
    @Autowired
    private CorreoFormatoRepo _Correo_FormatoRepo;

    public Valor find(Valor data) {
        return _ValorRepo.findOne(Example.of(data)).orElse(null);
    }

    public List<Valor> findIn(List<Valor> dataIn) {
        if (dataIn == null) {
            return null;
        }
        List<String> codigos = dataIn.stream().map(Valor::getCode).collect(Collectors.toList());
        return _ValorRepo.findByCodeIn(codigos);
    }

    public Valor findByCode(String codigo) {
        return _ValorRepo.findByCode(codigo);
    }

    public Valor save(Valor data) {
        return _ValorRepo.save(data);
    }

    public List<Valor> findAll(Valor data) {
        return _ValorRepo.findAll(Example.of(data));
    }



    public CorreoFormato find(CorreoFormato data) {
        return _Correo_FormatoRepo.findOne(Example.of(data)).orElse(null);
    }

    public CorreoFormato save(CorreoFormato data) {
        return _Correo_FormatoRepo.save(data);
    }

    public List<CorreoFormato> findAll(CorreoFormato data) {
        return _Correo_FormatoRepo.findAll(Example.of(data));
    }
}
