package tech.kood.match_me.matching.dto;

import java.util.Set;
import java.util.UUID;

public class UserDTO {

    private final UUID id;
    private final String username;
    private final int age;
    private final String bio;
    private final String profilepicSrc;
    private final HomeplanetDTO homeplanet;
    private final BodyformDTO bodyform;
    private final LookingForDTO lookingFor;
    private final Set<InterestDTO> interests;

    public UserDTO(UUID id, String username, int age, String bio, String profilepicSrc,
                   HomeplanetDTO homeplanet, BodyformDTO bodyform,
                   LookingForDTO lookingFor, Set<InterestDTO> interests) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.bio = bio;
        this.profilepicSrc = profilepicSrc;
        this.homeplanet = homeplanet;
        this.bodyform = bodyform;
        this.lookingFor = lookingFor;
        this.interests = interests;
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public int getAge() { return age; }
    public String getBio() { return bio; }
    public String getProfilepicSrc() { return profilepicSrc; }
    public HomeplanetDTO getHomeplanet() { return homeplanet; }
    public BodyformDTO getBodyform() { return bodyform; }
    public LookingForDTO getLookingFor() { return lookingFor; }
    public Set<InterestDTO> getInterests() { return interests; }
}