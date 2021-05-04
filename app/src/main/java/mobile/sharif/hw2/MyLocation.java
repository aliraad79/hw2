package mobile.sharif.hw2;


import java.io.Serializable;

public class MyLocation implements Serializable {
    private double longitude;
    private double latitude;
    private String name;

    public MyLocation(double longitude, double latitude, String name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    public MyLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        String lat = String.format("%.5f", latitude);
        String lon = String.format("%.5f", longitude);
        return lat + " " + lon;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
