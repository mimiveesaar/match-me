package tech.kood.match_me.matching.dto;

import java.util.List;

// DTO for incoming filter criteria

public class MatchFilter {
    private Integer minAge;
    private Integer maxAge;
    private String homeplanet;
    private String bodyform;
    private List<String> interests;
    private String lookingFor;
    private Double maxDistanceLy;

    public Integer getMinAge() {
        return minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public String getHomeplanet() {
        return homeplanet;
    }
   
    public String getBodyform() {
        return bodyform;
    }
    public List<String> getInterests() {
        return interests;
    }
 
    public String getLookingFor() {
        return lookingFor;
    }

    public Double getMaxDistanceLy() {
        return maxDistanceLy;
    }
}