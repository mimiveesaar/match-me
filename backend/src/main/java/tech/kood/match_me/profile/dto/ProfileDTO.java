package tech.kood.match_me.profile.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileDTO {
    private String username;
    private String name;
    private Integer age;

    @JsonProperty("homeplanetId")
    private Integer homeplanetId;

    @JsonProperty("bodyformId")
    private Integer bodyformId;

    @JsonProperty("lookingForId")
    private Integer lookingForId;

    private String bio;

    @JsonProperty("interestIds")
    private List<Integer> interestIds;

    @JsonProperty("profilePic")
    private String profilePic;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }


    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Integer getHomeplanetId() { return homeplanetId; }
    public void setHomeplanetId(Integer homeplanetId) { this.homeplanetId = homeplanetId; }

    public Integer getBodyformId() { return bodyformId; }
    public void setBodyformId(Integer bodyformId) { this.bodyformId = bodyformId; }

    public Integer getLookingForId() { return lookingForId; }
    public void setLookingForId(Integer lookingForId) { this.lookingForId = lookingForId; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public List<Integer> getInterestIds() { return interestIds; }
    public void setInterestIds(List<Integer> interestIds) { this.interestIds = interestIds; }

    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }

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
