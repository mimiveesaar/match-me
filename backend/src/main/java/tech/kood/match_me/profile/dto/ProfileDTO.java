package tech.kood.match_me.profile.dto;

import java.util.Set;
import java.util.UUID;

public class ProfileDTO {
    private UUID id;
    private String username;
    private Integer age;
    private Long homeplanetId;
    private Long bodyformId;
    private Long lookingForId;
    private String bio;
    private Set<Long> interestIds;
    private String profilePic;

    // getters & setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getHomeplanetId() {
        return homeplanetId;
    }

    public void setHomeplanetId(Long homeplanetId) {
        this.homeplanetId = homeplanetId;
    }

    public Long getBodyformId() {
        return bodyformId;
    }

    public void setBodyformId(Long bodyformId) {
        this.bodyformId = bodyformId;
    }

    public Long getLookingForId() {
        return lookingForId;
    }

    public void setLookingForId(Long lookingForId) {
        this.lookingForId = lookingForId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<Long> getInterestIds() {
        return interestIds;
    }

    public void setInterestIds(Set<Long> interestIds) {
        this.interestIds = interestIds;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

}
