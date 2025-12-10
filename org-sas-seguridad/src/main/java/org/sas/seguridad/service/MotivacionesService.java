package org.sas.seguridad.service;

import org.sas.seguridad.entity.Motivaciones;
import org.sas.seguridad.repository.MotivacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MotivacionesService {

    @Autowired
    private MotivacionesRepository repository;

    public Motivaciones consultarFraseMotivacion() {
        try {
            Long min = 1L;
            Long max = repository.count();
            int idRamdon = (int) (Math.random() * (max - min + 1) + min);
            System.out.println(idRamdon);
            return repository.getById(Integer.valueOf(idRamdon));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
