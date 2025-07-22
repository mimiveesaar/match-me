package tech.kood.match_me.matching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.matching.model.User;

@Repository
public interface MatchUserRepository extends JpaRepository<User, Long> {
    // You can add custom query methods here later if needed
}