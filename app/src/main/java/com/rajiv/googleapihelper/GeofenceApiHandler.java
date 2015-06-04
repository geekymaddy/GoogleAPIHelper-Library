package com.rajiv.googleapihelper;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.rajiv.googleapihelper.beans.GeofenceBean;
import com.rajiv.googleapihelper.intentservices.ReceiveTransitionsIntentService;
import com.rajiv.googleapihelper.interfaces.GeofenceInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 777938rvka on 01-06-2015.
 */
public class GeofenceApiHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private PendingIntent mGeofencePendingIntent;
    private Context context;
    private GeofenceInterface geofenceInterface;
    private List<GeofenceBean> geofenceBeanList;




    public GeofenceApiHandler(Context context, GeofenceInterface geofenceInterface) {
        this.context = context;
        this.geofenceInterface = geofenceInterface;
    }


    private BroadcastReceiver geofenceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent
                    .getExtras()
                    .get(ReceiveTransitionsIntentService.TRANSITION_TYPE)
                    .equals(context
                            .getString(R.string.geofence_transition_exited))) {
                if (geofenceInterface != null) {
                    geofenceInterface.onExit();
                }
            } else if (intent
                    .getExtras()
                    .get(ReceiveTransitionsIntentService.TRANSITION_TYPE)
                    .equals(context
                            .getString(R.string.geofence_transition_entered))) {
                if (geofenceInterface != null) {
                    geofenceInterface.onEntry();
                }
            }
        }
    };


    private PendingIntent getGeofencePendingIntent() {

        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(context,
                ReceiveTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent
        // back when
        // calling addGeofences() and removeGeofences().

        mGeofencePendingIntent = PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return mGeofencePendingIntent;
    }

    public void requestGeofenceUpdates(List<GeofenceBean> geofenceBeanList) {
        this.geofenceBeanList = geofenceBeanList;
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();

        LocalBroadcastManager.getInstance(context).registerReceiver(
                geofenceBroadcastReceiver, new IntentFilter(ReceiveTransitionsIntentService.INTENT_FILTER));

    }

    @Override
    public void onConnected(Bundle bundle) {

        registerGeofences();


    }

    private void registerGeofences() {

        if (googleApiClient.isConnected()) {
            LocationServices.GeofencingApi.addGeofences(googleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent());
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(getGeofenceList(geofenceBeanList));
        return builder.build();
    }


    private List<Geofence> getGeofenceList(List<GeofenceBean> geofenceBeanList) {
        List<Geofence> geofenceList = new ArrayList<Geofence>();
        if (geofenceBeanList != null && geofenceBeanList.size() > 0)

        {
            for (int i = 0; i < geofenceBeanList.size(); i++) {
                geofenceList.add(new Geofence.Builder()
                        // Set the request ID of the geofence. This is a string
                        // to identify this
                        // geofence.
                        .setRequestId(geofenceBeanList.get(i).getId())

                        .setCircularRegion(geofenceBeanList.get(i).getLatitude(),
                                geofenceBeanList.get(i).getLongitude(), geofenceBeanList.get(i).getRadius())
                        .setExpirationDuration(geofenceBeanList.get(i).getExpirationDuration())
                        .setTransitionTypes(geofenceBeanList.get(i).getTransitionType())
                        .build());
            }
        }
        return geofenceList;

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
