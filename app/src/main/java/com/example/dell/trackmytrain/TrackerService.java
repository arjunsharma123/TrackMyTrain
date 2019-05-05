package com.example.dell.trackmytrain;

import android.*;
import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class TrackerService extends Service {
    private static final String TAG = TrackerService.class.getSimpleName();
    public TrackerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        requestLocationUpdates();
        Toast.makeText(TrackerService.this, "Tracker Service Started", Toast.LENGTH_SHORT).show();
    }

    private void buildNotification() {
        String stop="stop";
        registerReceiver(stopReceiver,new IntentFilter(stop));
        PendingIntent broadcastIntent=PendingIntent.getBroadcast(this,0,new Intent(stop),PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(TrackerService.this)
                .setContentIntent(broadcastIntent)
                .setContentTitle(getString(R.string.name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true);
        startForeground(1,builder.build());

    }
BroadcastReceiver stopReceiver=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "received stop broadcast");
        unregisterReceiver(stopReceiver);
        stopSelf();
    }
};
   private  void requestLocationUpdates() {
       LocationRequest locationRequest=new LocationRequest();
       locationRequest.setInterval(10000);
       locationRequest.setFastestInterval(5000);
       locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       FusedLocationProviderClient client= LocationServices.getFusedLocationProviderClient(this);
      int permission= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
      if (permission== PackageManager.PERMISSION_GRANTED)
      {
          client.requestLocationUpdates(locationRequest ,new LocationCallback()
          {
              @Override
              public void onLocationResult(LocationResult locationResult) {
                  DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Tracker").child("1");
                  Location location = locationResult.getLastLocation();
                  if (location != null) {
                      Toast.makeText(TrackerService.this, "location update"+location, Toast.LENGTH_SHORT).show();

                      ref.setValue(location);
                  }
              }
          },null);

      }
    }

}
