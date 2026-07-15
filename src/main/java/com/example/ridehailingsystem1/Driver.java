package com.example.ridehailingsystem1;


public class Driver {
    private String id;
    private String name;
    private double rating;
    private String status;
    private Vehicle vehicle;

    public Driver(String id, String name, double rating, Vehicle vehicle) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.status = "AVAILABLE";
        this.vehicle = vehicle;
    }

    public void updateLocation(String location) {
        System.out.println("Driver " + name + " updated location to: " + location);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getRating() { return rating; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Vehicle getVehicle() { return vehicle; }
}
