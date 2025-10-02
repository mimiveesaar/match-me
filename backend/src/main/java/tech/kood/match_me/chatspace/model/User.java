package tech.kood.match_me.chatspace.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "profile_pic_src", length = 500)
    private String profilePicSrc;

    @Enumerated(EnumType.STRING)
    private UserStatus status; // ONLINE, OFFLINE

    private LocalDateTime lastActive;

    // Relationships
    @ManyToMany(mappedBy = "participants")
    private Set<Conversation> conversations;


    public UUID getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public void setProfilePicSrc(String profilePicSrc) {this.profilePicSrc = profilePicSrc;}
    public String getProfilePicSrc() {return profilePicSrc;}
    public UserStatus getStatus() {
        return status;
    }
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    public LocalDateTime getLastActive() {
        return lastActive;
    }
    public void setLastActive(LocalDateTime lastActive) {
        this.lastActive = lastActive;
    }
    public Set<Conversation> getConversations() {
        return conversations;
    }
    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }
}
