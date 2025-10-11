package tech.kood.match_me.profile.dto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProfileViewDTO {
    private UUID id;
    private String username;
    private String name;
    private Integer age;
    private String homeplanet;
    private UUID homeplanetId; // Changed from Integer to UUID
    private String bodyform;
    private UUID bodyformId; // Changed from Integer to UUID
    private String lookingFor;
    private UUID lookingForId; // Changed from Integer to UUID
    private String bio;
    private Set<String> interests;
    private List<UUID> interestIds; // Changed from Integer to UUID
    private String profilePic;

    // Getters and Setters
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

    public String getHomeplanet() {
        return homeplanet;
    }

    public void setHomeplanet(String homeplanet) {
        this.homeplanet = homeplanet;
    }

    public UUID getHomeplanetId() {
        return homeplanetId;
    }

    public void setHomeplanetId(UUID homeplanetId) {
        this.homeplanetId = homeplanetId;
    }

    public String getBodyform() {
        return bodyform;
    }

    public void setBodyform(String bodyform) {
        this.bodyform = bodyform;
    }

    public UUID getBodyformId() {
        return bodyformId;
    }

    public void setBodyformId(UUID bodyformId) {
        this.bodyformId = bodyformId;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
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

    public Set<String> getInterests() {
        return interests;
    }

    public void setInterests(Set<String> interests) {
        this.interests = interests;
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
}