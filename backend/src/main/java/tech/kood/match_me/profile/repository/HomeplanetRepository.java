package tech.kood.match_me.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.profile.model.Homeplanet;

import java.util.UUID;

@Repository
public interface HomeplanetRepository extends JpaRepository<Homeplanet, UUID> {
    boolean existsByName(String name);
}