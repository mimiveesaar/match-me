package tech.kood.match_me.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.profile.model.LookingFor;


@Repository
public interface LookingForRepository extends JpaRepository<LookingFor, Integer> {
    boolean existsByName(String name);
}