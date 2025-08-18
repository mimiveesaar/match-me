package tech.kood.match_me.profile.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import tech.kood.match_me.profile.dto.ProfileFilter;
import tech.kood.match_me.profile.model.Bodyform;
import tech.kood.match_me.profile.model.Homeplanet;
import tech.kood.match_me.profile.model.Interest;
import tech.kood.match_me.profile.model.LookingFor;
import tech.kood.match_me.profile.model.User;

@Repository
public class ProfileUserRepositoryImpl implements ProfileUserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findByFilter(ProfileFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        Join<User, Homeplanet> homeplanetJoin = root.join("homeplanet", JoinType.INNER);

        // --- Optional: other filters ---
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
            query.distinct(true);
        }

        // --- Distance filter ---
        if (filter.getHomeplanet() != null && filter.getMaxDistanceLy() != null) {
            // lookup coordinates of the selected planet
            CriteriaQuery<Homeplanet> hpQuery = cb.createQuery(Homeplanet.class);
            Root<Homeplanet> hpRoot = hpQuery.from(Homeplanet.class);
            hpQuery.select(hpRoot).where(cb.equal(hpRoot.get("name"), filter.getHomeplanet()));
            Homeplanet selectedPlanet = entityManager.createQuery(hpQuery).getSingleResult();

            double homeLat = selectedPlanet.getLatitude();
            double homeLon = selectedPlanet.getLongitude();
            double maxDistance = filter.getMaxDistanceLy();

            // Get the user's planet coordinates (not filter by planet name!)
            Expression<Double> userLat = homeplanetJoin.get("latitude");
            Expression<Double> userLon = homeplanetJoin.get("longitude");

            // Calculate distance between user's planet and selected planet
            Expression<Double> latDiff = cb.diff(userLat, homeLat);
            Expression<Double> lonDiff = cb.diff(userLon, homeLon);

            Expression<Double> latDiffSq = cb.prod(latDiff, latDiff);
            Expression<Double> lonDiffSq = cb.prod(lonDiff, lonDiff);

            Expression<Double> sumSq = cb.sum(latDiffSq, lonDiffSq);
            Expression<Double> distance = cb.function("SQRT", Double.class, sumSq);

            // Only add the distance constraint - NOT the planet name constraint
            predicates.add(cb.le(distance, maxDistance));
        } else if (filter.getHomeplanet() != null) {
            predicates.add(cb.equal(homeplanetJoin.get("name"), filter.getHomeplanet()));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}