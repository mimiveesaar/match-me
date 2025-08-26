package tech.kood.match_me.profile.dto;

import java.util.Set;
import java.util.UUID;

public class ProfileViewDTO {
    private UUID id;
    private String username;
    private int age;
    private String homeplanet;
    private String bodyform;
    private String lookingFor;
    private String bio;
    private Set<String> interests;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHomeplanet() {
        return homeplanet;
    }

    public void setHomeplanet(String homeplanet) {
        this.homeplanet = homeplanet;
    }

    public String getBodyform() {
        return bodyform;
    }

    public void setBodyform(String bodyform) {
        this.bodyform = bodyform;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}