package tech.kood.match_me.matching.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.matching.model.RejectionRequest;


@Repository
public interface RejectionRequestRepository extends JpaRepository<RejectionRequest, UUID> {
    List<RejectionRequest> findAllByRequesterId(UUID requesterId);
}

