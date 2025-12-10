package org.origami.docs.repository;

import org.origami.docs.entity.GCTipoContenido;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GCTipoContenidoRepository  extends MongoRepository<GCTipoContenido, String> {
}
