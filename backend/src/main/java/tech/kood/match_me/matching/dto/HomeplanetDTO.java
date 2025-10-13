package tech.kood.match_me.matching.dto;

import tech.kood.match_me.profile.model.Homeplanet;

public class HomeplanetDTO {
    private final Integer id;
    private final String name;
    private final Float latitude;
    private final Float longitude;

    public HomeplanetDTO(Integer id, String name, Float  latitude, Float longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Integer getId() { return id; }
    public String getName() { return name; }
    public Float getLatitude() { return latitude; }
    public Float getLongitude() { return longitude; }
}
