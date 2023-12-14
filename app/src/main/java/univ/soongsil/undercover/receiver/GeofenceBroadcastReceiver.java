package univ.soongsil.undercover.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;


public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "GEOFENCE";
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent == null) {
            Log.e(TAG, "geofence event is missing");
            return;
        }
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
                || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            if (triggeringGeofences == null) return;
            ArrayList<String> names = new ArrayList<>();
            for (Geofence geofence : triggeringGeofences) {
                names.add(geofence.getRequestId());
            }

            Intent sendIntent = new Intent("geofence_result");
            sendIntent.putExtra("names", names);
            sendIntent.putExtra("action", geofenceTransition);
            LocalBroadcastManager.getInstance(context).sendBroadcast(sendIntent);
        }

    }
}
