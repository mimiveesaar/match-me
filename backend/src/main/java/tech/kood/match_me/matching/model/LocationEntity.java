package tech.kood.match_me.matching.model;

import jakarta.persistence.Embeddable;


@Embeddable
public class LocationEntity {

    private double lat;
    private double lng;

    public LocationEntity() {
    }

    public LocationEntity(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
