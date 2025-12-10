package org.sas.mail.repository;

import org.sas.mail.entity.CorreoSettings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CorreoSettingsRepository extends MongoRepository<CorreoSettings, String> {

    CorreoSettings findFirstByOrderByIdDesc();

    CorreoSettings findByCorreo(String correo);

    CorreoSettings findByCodigo(String codigo);

}
