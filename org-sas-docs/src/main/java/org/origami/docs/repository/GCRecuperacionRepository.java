package org.origami.docs.repository;

import org.origami.docs.entity.GCRecuperacion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GCRecuperacionRepository extends MongoRepository<GCRecuperacion, String> {
}
