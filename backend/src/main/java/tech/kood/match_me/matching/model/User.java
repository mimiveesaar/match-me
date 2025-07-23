package tech.kood.match_me.matching.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String username;

    private int age;

    private String homeplanet;

    private String bodyform;

    @ElementCollection
    private List<String> interests;

    private String bio;

    private String lookingFor;

    // --- Constructors ---

    public User() {}

    public User(String username, int age, String homeplanet, String bodyform, List<String> interests, String bio, String lookingFor) {
        this.username = username;
        this.age = age;
        this.homeplanet = homeplanet;
        this.bodyform = bodyform;
        this.interests = interests;
        this.bio = bio;
        this.lookingFor = lookingFor;
    }

    // --- Getters and Setters ---

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }   

    public int getAge() {
        return age;
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

    public String getBio() {
        return bio;
    }

    public String getLookingFor() {
        return lookingFor;
    }
}