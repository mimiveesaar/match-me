package tech.kood.match_me.matching.dto;

import tech.kood.match_me.matching.model.User;

public class MatchResultDto {
    private User profile;
    private double score;
    private boolean supermatch;

    public MatchResultDto(User profile, double score, boolean supermatch) {
        this.profile = profile;
        this.score = score;
        this.supermatch = supermatch;
    }

    public User getProfile() {
        return profile;
    }

    public double getScore() {
        return score;
    }

    public boolean isSupermatch() {
        return supermatch;
    }
}
