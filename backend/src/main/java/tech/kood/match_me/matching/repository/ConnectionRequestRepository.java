package tech.kood.match_me.matching.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.matching.model.ConnectionRequestEntity;

@Repository("MatchingConnectionRequestRepository")
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequestEntity, UUID>{

    List<ConnectionRequestEntity> findAllByRequesterId(UUID requesterId);
    
}
