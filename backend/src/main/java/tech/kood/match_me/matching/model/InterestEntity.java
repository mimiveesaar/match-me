package tech.kood.match_me.matching.model;

import jakarta.persistence.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "interests")
public class InterestEntity {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "interest")
    private String name;

    @ManyToMany(mappedBy = "interests")
    @JsonBackReference
    private Set<UserEntity> users;

    public InterestEntity() {
    }

    public InterestEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public InterestEntity(Integer id) {
        this.id = id;
    }

    public InterestEntity(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }
}
