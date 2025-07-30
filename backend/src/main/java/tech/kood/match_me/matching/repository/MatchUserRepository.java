package tech.kood.match_me.matching.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.matching.model.User;

@Repository
public interface MatchUserRepository extends JpaRepository<User, UUID>, MatchUserRepositoryCustom {
    // Now supports findByFilter()
}