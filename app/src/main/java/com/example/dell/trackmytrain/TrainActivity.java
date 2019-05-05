package com.example.dell.trackmytrain;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.widget.Toast.LENGTH_SHORT;

public class TrainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Toast.makeText(this, "Please enable location services", LENGTH_SHORT).show();
            finish();
        }
        int permission= ActivityCompat.checkSelfPermission(TrainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(permission== PackageManager.PERMISSION_GRANTED)
        {
            startTrackerService();

        }
        else
        {
            ActivityCompat.requestPermissions(TrainActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    private void startTrackerService() {
        startService(new Intent(TrainActivity.this,TrackerService.class));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {startTrackerService();
        }
        else {
            finish();
        }
    }
}
