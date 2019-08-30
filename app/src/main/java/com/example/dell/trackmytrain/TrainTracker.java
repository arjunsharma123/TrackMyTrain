package com.example.dell.trackmytrain;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class TrainTracker extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    DatabaseReference reference1,reference2;
    Polyline polyline;
    LatLng location1,location2;
    MarkerOptions marker1,marker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_tracker);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(TrainTracker.this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        subscribeToUpdates();
    }



    private void subscribeToUpdates() {
        reference1 = FirebaseDatabase.getInstance().getReference().child("Tracker").child("1");
        reference2=FirebaseDatabase.getInstance().getReference().child("Tracker").child("2");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double lat = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                double lng = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                location1 = new LatLng(lat, lng);
                marker1=new MarkerOptions().position(location1);
                mMap.addMarker(marker1);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location1));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(0));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double lat = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                double lng = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                location2 = new LatLng(lat, lng);
                marker2=new MarkerOptions().position(location2);
                mMap.addMarker(marker2);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location2));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(0));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
getURL(location1,location2);
    }
String getURL(LatLng location1,LatLng location2)
{
    String source=location1.latitude+","+location1.longitude;
    String destination=location2.latitude+","+location2.longitude;
    return "https://maps.googleapis.com/maps/api/directions/json?origin="+source+"&destination="+destination+"&key=GOOGLE_API_KEY";

}
private String requestDirection(String reqUrl) throws IOException {
String response="";
    InputStream inputStream=null;
    HttpURLConnection httpURLConnection=null;
    try {
        URL url=new URL(reqUrl);
        httpURLConnection=(HttpURLConnection) url.openConnection();
        httpURLConnection.connect();
        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer=new StringBuffer();
        String line="";
        while ((line=bufferedReader.readLine())!=null)
        {
            stringBuffer.append(line);
        }
        response=stringBuffer.toString();
        bufferedReader.close();
        inputStreamReader.close();
    }
    catch (Exception e)
    {

    }
    finally {
        if(inputStream!=null)
        {
            inputStream.close();
        }
        httpURLConnection.disconnect();
    }
    return response;
}

public class TaskRequestDirection extends AsyncTask<String,Void,String>
{

    @Override
    protected String doInBackground(String... strings) {
        String response="";
        try {
            response=requestDirection(strings[0]);

        }
        catch (IOException e)
        {

        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

}
