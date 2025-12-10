package org.sas.mail.repository;

import org.sas.mail.entity.CorreoFormato;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CorreoFormatoRepository extends MongoRepository<CorreoFormato, String>, PagingAndSortingRepository<CorreoFormato, String> {
}
