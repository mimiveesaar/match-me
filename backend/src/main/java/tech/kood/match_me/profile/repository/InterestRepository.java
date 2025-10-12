package tech.kood.match_me.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.profile.model.Interest;

import java.util.UUID;

@Repository
public interface InterestRepository extends JpaRepository<Interest, UUID> {
    boolean existsByName(String name);
}