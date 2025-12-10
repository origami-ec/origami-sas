package org.origami.docs.repository;

import org.origami.docs.entity.origamigt.talentoHumano.Servidor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ServidorRepository extends MongoRepository<Servidor, String> {

    List<Servidor> findAllByIdServidor(Long idServidor);
}
