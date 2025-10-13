package tech.kood.match_me.matching.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "age")
    private int age;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "homeplanet_id", referencedColumnName = "id")
    private HomeplanetEntity homeplanet;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bodyform_id", referencedColumnName = "id")
    private BodyformEntity bodyform;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "looking_for_id", referencedColumnName = "id")
    private LookingForEntity lookingFor;

    @Column(name = "bio")
    private String bio;

    @Column(name = "profilepic_src")  
    private String profilepicSrc;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_interests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    @JsonManagedReference
    private Set<InterestEntity> interests = new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(UUID id, String username, int age, HomeplanetEntity homeplanet, BodyformEntity bodyform, String bio, LookingForEntity lookingFor, Set<InterestEntity> interests, String profilepicSrc) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.homeplanet = homeplanet;
        this.bodyform = bodyform;
        this.bio = bio;
        this.lookingFor = lookingFor;
        this.interests = interests;
        this.profilepicSrc = profilepicSrc;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public HomeplanetEntity getHomeplanet() {
        return homeplanet;
    }

    public BodyformEntity getBodyform() {
        return bodyform;
    }

    public String getBio() {
        return bio;
    }

    public LookingForEntity getLookingFor() {
        return lookingFor;
    }

    public Set<InterestEntity> getInterests() {
        return interests;
    }

    public String getProfilepicSrc() {
        return profilepicSrc;
    }
}
