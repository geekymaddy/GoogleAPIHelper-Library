package com.rajiv.googleapihelper.interfaces;

/**
 * Created by 777938rvka on 01-06-2015.
 */
import android.location.Location;

public interface LocationUpdateListener {
    void onNewLocationReceived(Location loc);
}