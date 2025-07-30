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
    private EntityManager em;

    @Override
    public List<User> findByFilter(MatchFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getMinAge() != null) {
            predicates.add(cb.greaterThanOrEqualTo(user.get("age"), filter.getMinAge()));
        }

        if (filter.getMaxAge() != null) {
            predicates.add(cb.lessThanOrEqualTo(user.get("age"), filter.getMaxAge()));
        }

        if (filter.getHomeplanet() != null) {
            predicates.add(cb.equal(user.get("homeplanet"), filter.getHomeplanet()));
        }

        if (filter.getBodyform() != null) {
            predicates.add(cb.equal(user.get("bodyform"), filter.getBodyform()));
        }

        if (filter.getLookingFor() != null) {
            predicates.add(cb.equal(user.get("lookingFor"), filter.getLookingFor()));
        }

        // Optional: handle interests if needed

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}
