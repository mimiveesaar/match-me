package tech.kood.match_me.matching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.matching.model.BodyformEntity;

@Repository("MatchingBodyformRepository")
public interface BodyformRepository extends JpaRepository<BodyformEntity, Integer> {
}
