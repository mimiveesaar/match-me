package tech.kood.match_me.matching.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bodyforms")
public class BodyformEntity {

    @Id
    private Integer id;

    private String name;

    public BodyformEntity() {
    }

    public BodyformEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public BodyformEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
