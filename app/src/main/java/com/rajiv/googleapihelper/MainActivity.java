package com.rajiv.googleapihelper;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rajiv.googleapihelper.beans.GeofenceBean;
import com.rajiv.googleapihelper.interfaces.GeofenceInterface;
import com.rajiv.googleapihelper.interfaces.LocationUpdateListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationUpdateListener  locationUpdateListener = new LocationUpdateListener() {
            @Override
            public void onNewLocationReceived(Location location) {
                Toast.makeText(getApplicationContext(),"Location : "+location.getLatitude()+", "+location.getLongitude(),Toast.LENGTH_LONG).show();

                GeofenceApiHandler geofenceApiHandler = new GeofenceApiHandler(MainActivity.this,geofenceInterface);
                GeofenceBean geofenceBean = new GeofenceBean();
                geofenceBean.setId("1");
                geofenceBean.setLatitude(location.getLatitude());
                geofenceBean.setLongitude(location.getLongitude());
                geofenceBean.setExpirationDuration(GeofenceBean.NEVER_EXPIRE);
                geofenceBean.setRadius(100);
                geofenceBean.setTransitionType(GeofenceBean.GEOFENCE_ENTER_OR_EXIT);
                List<GeofenceBean> geofenceBeanList = new ArrayList<GeofenceBean>();
                geofenceBeanList.add(geofenceBean);
                geofenceApiHandler.requestGeofenceUpdates(geofenceBeanList);

            }
        };
        LocationApiHandler locationApiHandler = new LocationApiHandler(this,locationUpdateListener );
        locationApiHandler.requestLocationUpdates();
    }

    private GeofenceInterface geofenceInterface = new GeofenceInterface() {
        @Override
        public void onEntry() {
            Toast.makeText(getApplicationContext(),"Entry",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onExit() {
            Toast.makeText(getApplicationContext(),"Exit",Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
