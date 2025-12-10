package org.ibarra.repository;

import org.ibarra.entity.IdentityLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdentityLinkRepository extends JpaRepository<IdentityLink, String> {
    List<IdentityLink> findByProcInstId(String procInstId);
}
