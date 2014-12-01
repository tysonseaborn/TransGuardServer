package com.example.tyson.transguardserver;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class GcmMessageHandler extends IntentService {

    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if(extras.getString("regId") != null) {
            Toast.makeText(getBaseContext(), "Received : " + extras.getString("regId"), Toast.LENGTH_LONG).show();
            TransGuardServer.clientRegID = extras.getString("regId");
            Intent i = new Intent("trans");
            i.putExtra("method", "updateRegID");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } else if(extras.getString("lat") != null && extras.getString("lon") != null) {
            Log.i("GCM", "Received : (" + messageType +")  " + extras.getString("lat") + " " + extras.getString("lon"));
            Toast.makeText(getBaseContext(), "Received location: " + extras.getString("lat") + " " + extras.getString("lon"), Toast.LENGTH_LONG).show();
            TransGuardServer.lat = extras.getString("lat");
            TransGuardServer.lon = extras.getString("lon");
            Intent i = new Intent("loc");
            i.putExtra("method", "updateLocation");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}