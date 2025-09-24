package tech.kood.match_me.matching.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "age")
    private int age;

    @ManyToOne
    @JoinColumn(name = "homeplanet_id", referencedColumnName = "id")
    private Homeplanet homeplanet;

    @ManyToOne
    @JoinColumn(name = "bodyform_id", referencedColumnName = "id")
    private Bodyform bodyform;

    @ManyToOne
    @JoinColumn(name = "looking_for_id", referencedColumnName = "id")
    private LookingFor lookingFor;

    @Column(name = "bio")
    private String bio;

    @Column(name = "profilepic_src")  
    private String profilepicSrc;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_interests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    @JsonManagedReference
    private Set<Interest> interests = new HashSet<>();

    public User() {
    }

    public User(String username, int age, Homeplanet homeplanet, Bodyform bodyform, String bio, LookingFor lookingFor, Set<Interest> interests,  String profilepicSrc) {
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

    public Homeplanet getHomeplanet() {
        return homeplanet;
    }

    public Bodyform getBodyform() {
        return bodyform;
    }

    public String getBio() {
        return bio;
    }

    public LookingFor getLookingFor() {
        return lookingFor;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public String getProfilepicSrc() {
        return profilepicSrc;
    }
}
