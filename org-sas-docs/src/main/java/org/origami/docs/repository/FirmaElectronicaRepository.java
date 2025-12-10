package org.origami.docs.repository;

import org.origami.docs.entity.FirmaElectronica;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FirmaElectronicaRepository extends MongoRepository<FirmaElectronica, String> {


    FirmaElectronica findFirstByUsuario_ReferenceIdOrderByFechaCreacionDesc(Long usuario);

}
