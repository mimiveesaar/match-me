package tech.kood.match_me.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.profile.model.Interest;


@Repository
public interface InterestRepository extends JpaRepository<Interest, Integer> {
    boolean existsByName(String name);
}