package tech.kood.match_me.matching.dto;

import java.util.List;

public class MatchFilter {
    private int minAge;
    private int maxAge;
    private List<String> bodyform;
    private List<String> interests;
    private List<String> lookingFor;

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
    public List<String> getBodyform() {
        return bodyform;
    }
    public void setBodyform(List<String> bodyform) {
        this.bodyform = bodyform;
    }
    public List<String> getInterests() {
        return interests;
    }
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
    public List<String> getLookingFor() {
        return lookingFor;
    }
    public void setLookingFor(List<String> lookingFor) {
        this.lookingFor = lookingFor;
    }
}