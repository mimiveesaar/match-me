package tech.kood.match_me.matching.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import org.springframework.stereotype.Repository;

import tech.kood.match_me.matching.dto.MatchFilter;
import tech.kood.match_me.matching.model.Interest;
import tech.kood.match_me.matching.model.User;
import tech.kood.match_me.matching.model.Bodyform;
import tech.kood.match_me.matching.model.Homeplanet;

import java.util.ArrayList;
import java.util.List;

import tech.kood.match_me.matching.model.LookingFor;

@Repository
public class MatchUserRepositoryImpl implements MatchUserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findByFilter(MatchFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getHomeplanet() != null) {
            Join<User, Homeplanet> homeplanetJoin = root.join("homeplanet", JoinType.INNER);
            predicates.add(cb.equal(homeplanetJoin.get("name"), filter.getHomeplanet()));
        }

        if (filter.getLookingFor() != null) {
            Join<User, LookingFor> lookingForJoin = root.join("lookingFor", JoinType.INNER);
            predicates.add(cb.equal(lookingForJoin.get("name"), filter.getLookingFor()));
        }

        if (filter.getBodyform() != null) {
            Join<User, Bodyform> bodyformJoin = root.join("bodyform", JoinType.INNER);
            predicates.add(cb.equal(cb.lower(bodyformJoin.get("name")), filter.getBodyform().toLowerCase()));
        }

        if (filter.getMinAge() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("age"), filter.getMinAge()));
        }

        if (filter.getMaxAge() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("age"), filter.getMaxAge()));
        }

        if (filter.getInterests() != null && !filter.getInterests().isEmpty()) {
            Join<User, Interest> interestJoin = root.join("interests", JoinType.INNER);
            predicates.add(interestJoin.get("name").in(filter.getInterests()));
            query.distinct(true); // Use DISTINCT because joins can produce duplicate rows
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
