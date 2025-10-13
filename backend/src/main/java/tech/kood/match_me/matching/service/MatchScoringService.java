package tech.kood.match_me.matching.service;

import org.springframework.stereotype.Service;

import tech.kood.match_me.matching.model.HomeplanetEntity;
import tech.kood.match_me.matching.model.UserEntity;

@Service
public class MatchScoringService {

    private double calculateDistance(HomeplanetEntity a, HomeplanetEntity b) {
        double latDiff = a.getLatitude() - b.getLatitude();
        double lonDiff = a.getLongitude() - b.getLongitude();
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }

    public int calculateScore(UserEntity user, UserEntity candidate) {
        int score = 0;

        if (user.getHomeplanet().equals(candidate.getHomeplanet())) {
            score += 1;
        }

        if (user.getHomeplanet() != null && candidate.getHomeplanet() != null) {
            double distance = calculateDistance(user.getHomeplanet(), candidate.getHomeplanet());

            if (distance == 0) {
                score += 2;
            } else if (distance <= 50) {
                score += 1;
            } else if (distance <= 100) {
                score += 0.5;
            } 
        }

        if (user.getBodyform().equals(candidate.getBodyform())) {
            score += 1;
        }

        int sharedInterests = (int) user.getInterests().stream()
                .filter(candidate.getInterests()::contains)
                .count();

        if (sharedInterests == 2) {
            score += 0.5;
        } else if (sharedInterests > 2) {
            score += 1;
        }

        int ageDiff = Math.abs(user.getAge() - candidate.getAge());
        if (ageDiff <= 3) {
            score += 1;
        } else if (ageDiff <= 5) {
            score += 0.5;
        }

        if (user.getLookingFor().equals(candidate.getLookingFor())) {
            score += 1;
        }

        return score;
    }

    public boolean isSupermatch(double score) {
        return score >= 4; // Supermatch rule: 4–6 points = supermatch, the rest are just meehhh...
    }
}
