package tech.kood.match_me.matching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.matching.model.InterestEntity;

@Repository("MatchingInterestsRepository")
public interface InterestsRepository extends JpaRepository<InterestEntity, Integer> {
}
