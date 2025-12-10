package org.sas.administrativo.service.configuracion;


import org.sas.administrativo.entity.Canton;
import org.sas.administrativo.repository.configuracion.CantonRepository;
import org.sas.administrativo.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CantonService {

    @Autowired
    private CantonRepository repository;

    public Canton findCanto(Canton data) {
        Canton c = null;
        System.out.println("codigo>>" + data.getCodigo());
        try {
            c = repository.findOne(Example.of(data)).orElse(null);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public Canton guardar(Canton canton) {
        try {
            canton = repository.save(canton);
            return canton;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




}
