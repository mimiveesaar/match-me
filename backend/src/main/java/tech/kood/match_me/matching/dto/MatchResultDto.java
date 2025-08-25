package tech.kood.match_me.matching.dto;

import java.util.List;

import tech.kood.match_me.matching.model.Interest;
import tech.kood.match_me.matching.model.User;

// DTO for outgoing match results
public class MatchResultDto {
    private final String id;
    private final String username;
    private final Integer age;
    private final String homeplanet;
    private final String lookingFor;
    private final String bodyform;
    private final String bio;
    private final List<String> interests;
    private final double score;
    private final boolean supermatch;

    public MatchResultDto(User user, double score, boolean supermatch) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.age = user.getAge();
        this.homeplanet = user.getHomeplanet() != null ? user.getHomeplanet().getName() : null;
        this.lookingFor = user.getLookingFor() != null ? user.getLookingFor().getName() : null;
        this.bodyform = user.getBodyform() != null ? user.getBodyform().getName() : null;
        this.bio = user.getBio();
        this.interests = user.getInterests() != null
        ? user.getInterests().stream()
              .map(Interest::getName)
              .toList()
        : List.of();
        this.score = score;
        this.supermatch = supermatch;
    }

    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public Integer getAge() {
        return age;
    }
    public String getHomeplanet() {
        return homeplanet;
    }
    public String getLookingFor() {
        return lookingFor;
    }
    public String getBodyform() {
        return bodyform;
    }
    public String getBio() {
        return bio;
    }
    public List<String> getInterests() {
        return interests;
    }
    public double getScore() {
        return score;
    }
    public boolean isSupermatch() {
        return supermatch;
    }
}
