package tech.kood.match_me.matching.dto;

import java.util.List;

public class MatchFilter {
    private Integer minAge;
    private Integer maxAge;
    private Integer homeplanetId;
    private Integer bodyformId;
    private List<String> interests;
    private Integer lookingForId;

    // Getters and setters

    public Integer getMinAge() {
        return minAge;
    }
    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }
    public Integer getMaxAge() {
        return maxAge;
    }
    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }
    public Integer getHomeplanetId() {
        return homeplanetId;
    }
    public void setHomeplanetId(Integer homeplanet) {
        this.homeplanetId = homeplanet;
    }
    public Integer getBodyformId() {
        return bodyformId;
    }
    public void setBodyformId(Integer bodyform) {
        this.bodyformId = bodyform;
    }
    public List<String> getInterests() {
        return interests;
    }
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
    public Integer getLookingForId() {
        return lookingForId;
    }
    public void setLookingForId(Integer lookingFor) {
        this.lookingForId = lookingFor;
    }
}