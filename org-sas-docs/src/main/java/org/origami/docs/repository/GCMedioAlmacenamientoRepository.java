package org.origami.docs.repository;

import org.origami.docs.entity.GCMedioAlmacenamiento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GCMedioAlmacenamientoRepository extends MongoRepository<GCMedioAlmacenamiento, String> {
}
