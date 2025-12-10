package org.origami.docs.repository;

import org.origami.docs.entity.HistoricoOrigamigt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoricoOrigamigtRepo extends MongoRepository<HistoricoOrigamigt, String> {


    List<HistoricoOrigamigt> findAllByReferenciaId(Long referencia);
}
