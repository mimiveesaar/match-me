package tech.kood.match_me.profile.dto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProfileDTO {
    private UUID id;
    private String username;
    private Integer age;
    private String homeplanet;
    private Integer homeplanetId;
    private String bodyform;
    private Integer bodyformId;
    private String lookingFor;
    private Integer lookingForId;
    private String bio;
    private Set<String> interests;
    private List<Integer> interestIds;
    private String profilePic;

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getHomeplanet() { return homeplanet; }
    public void setHomeplanet(String homeplanet) { this.homeplanet = homeplanet; }

    public Integer getHomeplanetId() { return homeplanetId; }
    public void setHomeplanetId(Integer homeplanetId) { this.homeplanetId = homeplanetId; }

    public String getBodyform() { return bodyform; }
    public void setBodyform(String bodyform) { this.bodyform = bodyform; }

    public Integer getBodyformId() { return bodyformId; }
    public void setBodyformId(Integer bodyformId) { this.bodyformId = bodyformId; }

    public String getLookingFor() { return lookingFor; }
    public void setLookingFor(String lookingFor) { this.lookingFor = lookingFor; }

    public Integer getLookingForId() { return lookingForId; }
    public void setLookingForId(Integer lookingForId) { this.lookingForId = lookingForId; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Set<String> getInterests() { return interests; }
    public void setInterests(Set<String> interests) { this.interests = interests; }

    public List<Integer> getInterestIds() { return interestIds; }
    public void setInterestIds(List<Integer> interestIds) { this.interestIds = interestIds; }

    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }
<<<<<<< HEAD

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "username='" + username + '\'' +
                ", age=" + age +
                ", homeplanetId=" + homeplanetId +
                ", bodyformId=" + bodyformId +
                ", lookingForId=" + lookingForId +
                ", bio='" + bio + '\'' +
                ", interestIds=" + interestIds +
                ", profilePic='" + profilePic + '\'' +
                '}';
    }
=======
>>>>>>> 4664acb (ProfileViewDTO to ProfileTDO)
}
