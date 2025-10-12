package tech.kood.match_me.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.profile.model.Homeplanet;


@Repository
public interface HomeplanetRepository extends JpaRepository<Homeplanet, Integer> {
    boolean existsByName(String name);
}