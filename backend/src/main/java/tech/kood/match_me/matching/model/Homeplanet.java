package tech.kood.match_me.matching.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "homeplanets")
public class Homeplanet {

    @Id
    private Integer id;

    private String name;

    public Homeplanet() {
    }

    public Homeplanet(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
