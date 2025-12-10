package org.sas.mail.repository;

import org.sas.mail.entity.BrevoRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrevoRepository extends MongoRepository<BrevoRequest, String>, PagingAndSortingRepository<BrevoRequest, String> {

}
