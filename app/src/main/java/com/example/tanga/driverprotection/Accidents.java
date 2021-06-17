package com.example.tanga.driverprotection;

public class Accidents {

    private String CrashTime;
    private String Longitude;
    private String Latitude;

    public Accidents() {
    }

    public Accidents(String crashTime, String longitude, String latitude) {
        CrashTime = crashTime;
        Longitude = longitude;
        Latitude = latitude;
    }

    public String getCrashTime() {
        return CrashTime;
    }

    public void setCrashTime(String crashTime) {
        CrashTime = crashTime;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    @Override
    public String toString() {
        return "Accidents{" +
                "CrashTime='" + CrashTime + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", Latitude='" + Latitude + '\'' +
                '}';
    }
}
