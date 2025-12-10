package org.origami.docs.service;

import org.jfree.util.Log;
import org.origami.docs.entity.origamigt.talentoHumano.Servidor;
import org.origami.docs.repository.ServidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServidorServices {
    @Autowired
    private ServidorRepository repository;

    public Servidor guardar(Servidor data) {
        try {
            return repository.save(data);
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }

}
