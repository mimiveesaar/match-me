package tech.kood.match_me.matching;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int age;

    @Embedded
    private Location location;

    private String bodyform;

    @ElementCollection
    private List<String> interests;

    @ElementCollection
    private List<String> lookingFor;

    // --- Constructors ---

    public User() {}

    public User(int age, Location location, String bodyform, List<String> interests, List<String> lookingFor) {
        this.age = age;
        this.location = location;
        this.bodyform = bodyform;
        this.interests = interests;
        this.lookingFor = lookingFor;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public List<String> getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(List<String> lookingFor) {
        this.lookingFor = lookingFor;
    }
}