package com.example.peterpan.nearme;

/**
 * Created by Peterpan on 5/7/2016 AD.
 */
public class Place {
    private Geometry geometry;
    private String icon;
    private String id;
    private String name;
    private String place_id;
    private String type;
    private String vicinity;

    public Place(Geometry geometry, String icon, String id, String name, String place_id, String type, String vicinity) {
        this.geometry = geometry;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.place_id = place_id;
        this.type = type;
        this.vicinity = vicinity;
    }

    @Override
    public String toString() {
        return "Place{" +
                "geometry=" + geometry +
                ", icon='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", place_id='" + place_id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public static class Geometry {
        private Location location;

        public Geometry(Location location) {
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public static class Location {
        private double lat;
        private double lng;

        public Location(String lat, String lng) {
            this.lat = Double.parseDouble(lat);
            this.lng = Double.parseDouble(lng);
        }

        public double getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }
    }
}
