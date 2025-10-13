package tech.kood.match_me.matching.dto;

import java.util.List;

import tech.kood.match_me.matching.model.InterestEntity;
import tech.kood.match_me.matching.model.UserEntity;

public class CurrentUserDto {

    private final String id;
    private final String username;
    private final Integer age;
    private final String homeplanet;
    private final String lookingFor;
    private final String bodyform;
    private final String bio;
    private final List<String> interests;

    public CurrentUserDto(UserEntity user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.age = user.getAge();
        this.homeplanet = user.getHomeplanet() != null ? user.getHomeplanet().getName() : null;
        this.lookingFor = user.getLookingFor() != null ? user.getLookingFor().getName() : null;
        this.bodyform = user.getBodyform() != null ? user.getBodyform().getName() : null;
        this.bio = user.getBio();
        this.interests = user.getInterests() != null
                ? user.getInterests().stream()
                        .map(InterestEntity::getName)
                        .toList()
                : List.of();
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
}
