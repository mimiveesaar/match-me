package tech.kood.match_me.matching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.matching.model.HomeplanetEntity;

@Repository
public interface PlanetsRepository extends JpaRepository<HomeplanetEntity, Integer> {

}
