package com.rajiv.googleapihelper.intentservices;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.rajiv.googleapihelper.R;
import com.rajiv.googleapihelper.utils.GeofenceUtils;
import com.rajiv.googleapihelper.utils.LocationServiceErrorMessages;


/**
 * Created by 777938rvka on 01-06-2015.
 *
 * This class receives geofence transition events from Location Services, in the
 * form of an Intent containing the transition type and geofence id(s) that
 * triggered the event.
 */
public class ReceiveTransitionsIntentService extends IntentService {
	public static final String TRANSITION_TYPE = "com.rajiv.googleapihelper.TRANSITION_EXIT";
	public static final String TRANSITION_ID = "com.rajiv.googleapihelper.TRANSITION_ID";
	public static final String INTENT_FILTER = "com.rajiv.googleapihelper.GEOFENCE_INTENT";


	private static String TAG = "ReceiveTransitionsIntentService";

	/**
	 * Sets an identifier for this class' background thread
	 */
	public ReceiveTransitionsIntentService() {
		super("ReceiveTransitionsIntentService");
	}

	/**
	 * Handles incoming intents
	 * 
	 * @param intent
	 *            The Intent sent by Location Services. This Intent is provided
	 *            to Location Services (inside a PendingIntent) when you call
	 *            addGeofences()
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		// Create a local broadcast Intent
		Intent broadcastIntent = new Intent();

		// Give it the category for all intents sent by the Intent Service
		broadcastIntent.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);

		// First check for errors
		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		if (geofencingEvent.hasError()) {

			// Get the error code
			int errorCode = geofencingEvent.getErrorCode();

			// Get the error message
			String errorMessage = LocationServiceErrorMessages.getErrorString(
                    this, errorCode);

			// Log the error
			Log.e(TAG,errorMessage);

			// Set the action and error message for the broadcast intent
			broadcastIntent
					.setAction(GeofenceUtils.ACTION_GEOFENCE_ERROR)
					.putExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS, errorMessage);

			// Broadcast the error *locally* to other components in this app
			LocalBroadcastManager.getInstance(this).sendBroadcast(
					broadcastIntent);

			// If there's no error, get the transition type and create a
			// notification
		} else {

			// Get the type of transition (entry or exit)
			int transition = geofencingEvent.getGeofenceTransition();

			// Test that a valid transition was reported
			if ((transition == Geofence.GEOFENCE_TRANSITION_ENTER)
					|| (transition == Geofence.GEOFENCE_TRANSITION_EXIT)) {

				// Post a notification
				List<Geofence> geofences = geofencingEvent
						.getTriggeringGeofences();
				String[] geofenceIds = new String[geofences.size()];
				for (int index = 0; index < geofences.size(); index++) {
					geofenceIds[index] = geofences.get(index).getRequestId();
				}
				String ids = TextUtils.join(
						GeofenceUtils.GEOFENCE_ID_DELIMITER, geofenceIds);
				String transitionType = getTransitionString(transition);

				//sendNotification(transitionType, ids);
				sendGeofenceBroadcast(transitionType, ids);



			} else {

			}
		}
	}

	private void sendGeofenceBroadcast(String transitionType, String id) {

		Intent intent = new Intent();
		intent.setAction(INTENT_FILTER);
		intent.putExtra(ReceiveTransitionsIntentService.TRANSITION_TYPE,
				transitionType);
		intent.putExtra(ReceiveTransitionsIntentService.TRANSITION_ID, id);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

	}

	/**
	 * Maps geofence transition types to their human-readable equivalents.
	 * 
	 * @param transitionType
	 *            A transition type constant defined in Geofence
	 * @return A String indicating the type of transition
	 */
	private String getTransitionString(int transitionType) {
		switch (transitionType) {

		case Geofence.GEOFENCE_TRANSITION_ENTER:
			return getString(R.string.geofence_transition_entered);

		case Geofence.GEOFENCE_TRANSITION_EXIT:
			return getString(R.string.geofence_transition_exited);

		default:
			return getString(R.string.geofence_transition_unknown);
		}
	}
}
