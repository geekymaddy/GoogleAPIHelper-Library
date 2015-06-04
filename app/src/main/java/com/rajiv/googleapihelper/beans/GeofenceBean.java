package com.rajiv.googleapihelper.beans;

import com.google.android.gms.location.Geofence;

/**
 * Created by 777938rvka on 01-06-2015.
 */
public class GeofenceBean {

    private String id;
    private double latitude;
    private double longitude;
    private float radius = 100;
    public static int GEOFENCE_ENTER = Geofence.GEOFENCE_TRANSITION_ENTER;
    public static int GEOFENCE_EXIT = Geofence.GEOFENCE_TRANSITION_EXIT;
    public static int GEOFENCE_ENTER_OR_EXIT = Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_ENTER;
    public static int GEOFENCE_DWELL = Geofence.GEOFENCE_TRANSITION_DWELL;
    public static long NEVER_EXPIRE = Geofence.NEVER_EXPIRE;
    private long expirationDuration;
    private int transitionType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        if (radius > 100)
            this.radius = radius;
    }

    public long getExpirationDuration() {
        return expirationDuration;
    }

    public void setExpirationDuration(long expirationDuration) {
        this.expirationDuration = expirationDuration;
    }

    public int getTransitionType() {
        return transitionType;
    }

    public void setTransitionType(int transitionType) {
        this.transitionType = transitionType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
