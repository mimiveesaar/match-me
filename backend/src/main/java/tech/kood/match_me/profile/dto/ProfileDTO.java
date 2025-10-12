package tech.kood.match_me.profile.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileDTO {
    private String username;
    private String name;
    private Integer age;
    
    @JsonProperty("homeplanetId")
    private UUID homeplanetId;
    
    @JsonProperty("bodyformId")
    private UUID bodyformId;
    
    @JsonProperty("lookingForId")
    private UUID lookingForId;
    
    private String bio;
    
    @JsonProperty("interestIds")
    private List<UUID> interestIds;
    
    @JsonProperty("profilePic")
    private String profilePic;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public UUID getHomeplanetId() {
        return homeplanetId;
    }

    public void setHomeplanetId(UUID homeplanetId) {
        this.homeplanetId = homeplanetId;
    }

    public UUID getBodyformId() {
        return bodyformId;
    }

    public void setBodyformId(UUID bodyformId) {
        this.bodyformId = bodyformId;
    }

    public UUID getLookingForId() {
        return lookingForId;
    }

    public void setLookingForId(UUID lookingForId) {
        this.lookingForId = lookingForId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<UUID> getInterestIds() {
        return interestIds;
    }

    public void setInterestIds(List<UUID> interestIds) {
        this.interestIds = interestIds;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", homeplanetId=" + homeplanetId +
                ", bodyformId=" + bodyformId +
                ", lookingForId=" + lookingForId +
                ", bio='" + bio + '\'' +
                ", interestIds=" + interestIds +
                ", profilePic='" + profilePic + '\'' +
                '}';
    }
}