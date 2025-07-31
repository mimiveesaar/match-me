package tech.kood.match_me.matching.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.matching.dto.MatchFilter;
import tech.kood.match_me.matching.model.User;

import java.util.ArrayList;
import java.util.List;

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

        if (filter.getHomeplanetId() != null) {
            predicates.add(cb.equal(root.get("homeplanetId"), filter.getHomeplanetId()));
        }

        if (filter.getLookingForId() != null) {
            predicates.add(cb.equal(root.get("lookingForId"), filter.getLookingForId()));
        }

        if (filter.getBodyformId() != null) {
            predicates.add(cb.equal(root.get("bodyformId"), filter.getBodyformId()));
        }

        if (filter.getMinAge() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("age"), filter.getMinAge()));
        }

        if (filter.getMaxAge() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("age"), filter.getMaxAge()));
        }

        // Add more filters here as needed

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
