package tech.kood.match_me.matching.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "homeplanets")
public class HomeplanetEntity {

    @Id
    private Integer id;

    private String name;

    private Float latitude;

    private Float longitude;

    public HomeplanetEntity() {
    }

    public HomeplanetEntity(Integer id, String name, Float latitude, Float longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public HomeplanetEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }
}
