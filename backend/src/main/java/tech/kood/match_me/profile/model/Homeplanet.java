package tech.kood.match_me.profile.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "homeplanets")
public class Homeplanet {

@Id
@Column(name = "id", nullable = false, unique = true)
private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    public Homeplanet() {}

    public Homeplanet(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}