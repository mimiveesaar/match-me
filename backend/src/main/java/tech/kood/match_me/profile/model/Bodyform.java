package tech.kood.match_me.profile.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bodyforms")
public class Bodyform {

    @Id
    private Integer id;

    private String name;

    public Bodyform() {
    }

    public Bodyform(Integer id, String name) {
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