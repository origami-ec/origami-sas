package org.origami.docs.repository;

import org.origami.docs.entity.GCRetencion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GCRetencionRepository extends MongoRepository<GCRetencion, String> {
}
