package tech.kood.match_me.matching.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    private String username;

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

    private String bio;

    @ManyToMany
    @JoinTable(
            name = "user_interests", 
            joinColumns = @JoinColumn(name = "user_id"), 
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private Set<Interest> interests = new HashSet<>();

    public User() {
    }

    public User(String username, int age, Homeplanet homeplanet, Bodyform bodyform, String bio, LookingFor lookingFor, Set<Interest> interests) {
        this.username = username;
        this.age = age;
        this.homeplanet = homeplanet;
        this.bodyform = bodyform;
        this.bio = bio;
        this.lookingFor = lookingFor;
        this.interests = interests;
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

    public Bodyform bodyform() {
        return bodyform;
    }

    public String getBio() {
        return bio;
    }

    public LookingFor getLookingFor() {
        return lookingFor;
    }

    public Set<Interest> interests() {
        return interests;
    }
}
