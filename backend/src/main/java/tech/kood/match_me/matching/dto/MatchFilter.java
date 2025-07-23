package tech.kood.match_me.matching.dto;

import java.util.List;

public class MatchFilter {
    private int minAge;
    private int maxAge;
    private String homeplanet;
    private String bodyform;
    private List<String> interests;
    private String lookingFor;

    // Getters and setters

    public int getMinAge() {
        return minAge;
    }
    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }
    public int getMaxAge() {
        return maxAge;
    }
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
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
    public List<String> getInterests() {
        return interests;
    }
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
    public String getLookingFor() {
        return lookingFor;
    }
    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }
}