package tech.kood.match_me.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.profile.model.Bodyform;


@Repository
public interface BodyformRepository extends JpaRepository<Bodyform, Integer> {
    boolean existsByName(String name);
}