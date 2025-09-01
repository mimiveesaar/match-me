package tech.kood.match_me.matching.service;

import org.springframework.stereotype.Service;

import tech.kood.match_me.matching.model.User;


@Service
public class MatchScoringService {

    public int calculateScore(User user, User candidate) {
        int score = 0;

        if (user.getHomeplanet().equals(candidate.getHomeplanet())) score += 1;
        // if (candidate.getDistance() <= 50) score += 1;
        if (user.getBodyform().equals(candidate.getBodyform())) score += 1;

        int sharedInterests = (int) user.getInterests().stream()
            .filter(candidate.getInterests()::contains)
            .count();

        if (sharedInterests == 2) score += 0.5;
        else if (sharedInterests > 2) score += 1;

        int ageDiff = Math.abs(user.getAge() - candidate.getAge());
        if (ageDiff <= 3) score += 1;
        else if (ageDiff <= 5) score += 0.5;

        if (user.getLookingFor().equals(candidate.getLookingFor())) score += 1;

        return score;
    }

    public boolean isSupermatch(double score) {
        return score >= 4; // Supermatch rule: 4–6 points = supermatch, the rest are just meehhh...
    }
}
