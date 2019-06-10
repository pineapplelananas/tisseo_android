package com.example.tisseo;

public class Station {
    private String name;
    private String line_number;
    private String id_station;
    private String color;
    private String date;
    private String direction;

    public Station(String name, String line_number, String id_station, String color, String date, String direction) {
        this.name = name;
        this.line_number = line_number;
        this.id_station = id_station;
        this.color = color;
        this.date = date;
        this.direction = direction;
    }

    public Station(String name, String line_number, String id_station) {
        this.name = name;
        this.line_number = line_number;
        this.id_station = id_station;
    }
    public Station(String name, String line_number, String id_station, String color) {
        this.name = name;
        this.line_number = line_number;
        this.id_station = id_station;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine_number() {
        return line_number;
    }

    public void setLine_number(String line_number) {
        this.line_number = line_number;
    }

    public String getId_station() {
        return id_station;
    }

    public void setId_station(String id_station) {
        this.id_station = id_station;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
