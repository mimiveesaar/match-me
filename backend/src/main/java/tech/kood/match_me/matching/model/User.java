package tech.kood.match_me.matching.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    private String username;

    private int age;

    @Column(name = "homeplanetid")
    private Integer homeplanetId;

    @Column(name = "bodyformid")
    private Integer bodyformId;

    @Column(name = "lookingforid")
    private Integer lookingForId;

    private String bio;

    public User() {}

    public User(String username, int age, Integer homeplanetId, Integer bodyformId, String bio, Integer lookingForId) {
        this.username = username;
        this.age = age;
        this.homeplanetId = homeplanetId;
        this.bodyformId = bodyformId;
        this.bio = bio;
        this.lookingForId = lookingForId;
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

    public Integer getHomeplanetId() {
        return homeplanetId;
    }

    public Integer getBodyformId() {
        return bodyformId;
    }

    public String getBio() {
        return bio;
    }

    public Integer getLookingForId() {
        return lookingForId;
    }
}