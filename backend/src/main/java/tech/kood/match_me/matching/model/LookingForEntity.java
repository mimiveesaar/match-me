package tech.kood.match_me.matching.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "looking_for")
public class LookingForEntity {

    @Id
    private Integer id;

    private String name;

    public LookingForEntity() {
    }

    public LookingForEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public LookingForEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }  
}
