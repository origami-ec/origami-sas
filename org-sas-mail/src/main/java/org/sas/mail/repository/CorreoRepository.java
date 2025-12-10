package org.sas.mail.repository;

import org.sas.mail.entity.Correo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface CorreoRepository extends MongoRepository<Correo, String>, PagingAndSortingRepository<Correo, String> {



    @Query("{'fechaEnvio': { $gte: ?0, $lte: ?1 }, 'reenviado': { $in: [false, null] }, 'destinatario': { $not: { $regex: '@ibarra.gob.ec' } }, 'asunto': { $not: { $regex: 'Inicio de trámite$' } }}")
    List<Correo> findByFechaEnvioBetweenAndReenviadoAndDestinatarioAndAsunto(Date startDate, Date endDate);


}
