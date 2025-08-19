package tech.kood.match_me.matching.dto;

import java.util.List;
import java.util.UUID;

public class MatchFilter {
    private UUID userId; // ID of the user making the request
    private Integer minAge;
    private Integer maxAge;
    private String homeplanet;
    private String bodyform;
    private List<String> interests;
    private String lookingFor;
    private Double maxDistanceLy;

    public UUID getUserId() {
        return userId;
    }

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