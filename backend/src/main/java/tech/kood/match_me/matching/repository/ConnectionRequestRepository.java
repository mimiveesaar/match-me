package tech.kood.match_me.matching.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.matching.model.ConnectionRequest;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, UUID>{

    List<ConnectionRequest> findAllByRequesterId(UUID requesterId);
    
}
