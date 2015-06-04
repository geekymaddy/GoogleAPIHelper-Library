package com.rajiv.googleapihelper;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.rajiv.googleapihelper.interfaces.LocationUpdateListener;

/**
 * Created by 777938rvka on 01-06-2015.
 *
 * This class acts as an entry point to location services
 */
public class LocationApiHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;
    private Context context;
    LocationUpdateListener locationUpdateListener;


    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            locationUpdateListener.onNewLocationReceived(location);
        }

    };

    public LocationApiHandler(Context context, LocationUpdateListener locationUpdateListener) {

        this.context = context;
        this.locationUpdateListener = locationUpdateListener;


    }

    public void requestLocationUpdates() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();

    }

    /**
     * Todo Need to make
     */
    private void registerLocationUpdates() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 10 seconds
        mLocationRequest.setInterval(10000);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, locationListener);

    }

    private void unRegisterLocationUpdates() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, locationListener);
            googleApiClient.unregisterConnectionCallbacks(this);
            googleApiClient.unregisterConnectionFailedListener(this);
            googleApiClient.disconnect();
            googleApiClient = null;
        }

    }


    @Override
    public void onConnected(Bundle bundle) {
        if(googleApiClient.isConnected())
        {
            registerLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}
