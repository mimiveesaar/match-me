package tech.kood.match_me.profile.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "profiles")
public class Profile {


    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id; // 👈 from user management modul

    @Column(nullable = true, unique = false)
    private String name;

    @Column(nullable = true)
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeplanet_id")
    private Homeplanet homeplanet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bodyform_id")
    private Bodyform bodyform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "looking_for_id")
    private LookingFor lookingFor;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "profile_interests", joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id"))
    private Set<Interest> interests = new HashSet<>();

    @Column(name = "profile_pic")
    private String profilePic;

    // --- Constructors ---
    public Profile() {}

    public Profile(String name, int age, Homeplanet homeplanet, Bodyform bodyform,
            LookingFor lookingFor, String bio, Set<Interest> interests, String profilePic) {
        this.name = name;
        this.age = age;
        this.homeplanet = homeplanet;
        this.bodyform = bodyform;
        this.lookingFor = lookingFor;
        this.bio = bio;
        this.interests = interests != null ? interests : new HashSet<>();
        this.profilePic = profilePic;
    }

    // --- Getters & Setters ---
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Homeplanet getHomeplanet() {
        return homeplanet;
    }

    public void setHomeplanet(Homeplanet homeplanet) {
        this.homeplanet = homeplanet;
    }

    public Bodyform getBodyform() {
        return bodyform;
    }

    public void setBodyform(Bodyform bodyform) {
        this.bodyform = bodyform;
    }

    public LookingFor getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(LookingFor lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests != null ? interests : new HashSet<>();
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
