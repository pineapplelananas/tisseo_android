package com.example.tisseo;

public class StopTrip {
    private String id;
    private String city_name;
    private String destination;
    private String hour_id;
    private String ligne;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getHour_id() {
        return hour_id;
    }

    public void setHour_id(String hour_id) {
        this.hour_id = hour_id;
    }

    public String getLigne() {
        return ligne;
    }

    public void setLigne(String ligne) {
        this.ligne = ligne;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StopTrip(String id, String city_name, String destination, String hour_id, String ligne, String name) {
        this.id = id;
        this.city_name = city_name;
        this.destination = destination;
        this.hour_id = hour_id;
        this.ligne = ligne;
        this.name = name;
    }

    private String name;
}
