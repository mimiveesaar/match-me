package tech.kood.match_me.profile.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    private int age;

    @ManyToOne
    @JoinColumn(name = "homeplanet_id")
    private Homeplanet homeplanet;

    @ManyToOne
    @JoinColumn(name = "bodyform_id")
    private Bodyform bodyform;

    @ManyToOne
    @JoinColumn(name = "looking_for_id")
    private LookingFor lookingFor;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @ManyToMany
    @JoinTable(
        name = "profile_interests",
        joinColumns = @JoinColumn(name = "profile_id"),
        inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private Set<Interest> interests = new HashSet<>();

    @Column(name = "profile_pic")
    private String profilePic;

    // --- Constructors ---
    public Profile() {}

    public Profile(UUID id, String username, int age, Homeplanet homeplanet, 
                   Bodyform bodyform, LookingFor lookingFor, 
                   String bio, Set<Interest> interests, String profilePic) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.homeplanet = homeplanet;
        this.bodyform = bodyform;
        this.lookingFor = lookingFor;
        this.bio = bio;
        this.interests = interests;
        this.profilePic = profilePic;
    }

    // --- Getters and Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Homeplanet getHomeplanet() { return homeplanet; }
    public void setHomeplanet(Homeplanet homeplanet) { this.homeplanet = homeplanet; }

    public Bodyform getBodyform() { return bodyform; }
    public void setBodyform(Bodyform bodyform) { this.bodyform = bodyform; }

    public LookingFor getLookingFor() { return lookingFor; }
    public void setLookingFor(LookingFor lookingFor) { this.lookingFor = lookingFor; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Set<Interest> getInterests() { return interests; }
    public void setInterests(Set<Interest> interests) { this.interests = interests; }

    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }
}
