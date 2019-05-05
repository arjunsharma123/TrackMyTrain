package com.example.dell.trackmytrain;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ProgressDialog mMainProgress;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private AutoCompleteTextView mSearchTo, mSearchFrom,mCheckAll;
    private Button mSearchBtn,mCheckBtn;
    private NavigationView navigationView;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference reference;
    ArrayList<String> name,code;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mSearchFrom = findViewById(R.id.mSearchFrom);
        mSearchTo = findViewById(R.id.mSearchTo);
        mSearchBtn = findViewById(R.id.mSearchBtn);
        mCheckAll=findViewById(R.id.mCheckAll);
        mCheckBtn=findViewById(R.id.mCheckBtn);
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        final Menu nav_menu=navigationView.getMenu();
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        final boolean first = preferences.getBoolean("first", true);
        if (first) {
            showSlashActivity();
        }
        name=new ArrayList<>();
        code=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<String>(this,R.layout.searchlayout,name);
        mSearchFrom.setThreshold(1);
        mSearchTo.setThreshold(1);
        mSearchFrom.setAdapter(arrayAdapter);
        mSearchTo.setAdapter(arrayAdapter);
        mCheckAll.setThreshold(1);
        mCheckAll.setAdapter(arrayAdapter);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TrackMyTrain/station.txt"));
            String line="";
            while((line=bufferedReader.readLine())!=null)
            {
                name.add(line.substring(0,line.lastIndexOf(" ")));
                code.add(line.substring(line.lastIndexOf(" ")+1));
                arrayAdapter.notifyDataSetChanged();
            }


        }
        catch (FileNotFoundException e)
        {

        }
        catch (IOException i)
        {

        }

        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            nav_menu.findItem(R.id.drawer_logout_btn).setVisible(false);
            nav_menu.findItem(R.id.enable_tracking).setVisible(false);

        }
        else
        {
            nav_menu.findItem(R.id.drawer_signup_btn).setVisible(false);
            nav_menu.findItem(R.id.drawer_login_btn).setVisible(false);
            nav_menu.findItem(R.id.enable_tracking).setVisible(false);

        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId()) {

                    case R.id.enable_tracking:
                        Intent enableTrackingIntent=new Intent(MainActivity.this,TrainActivity.class);
                        startActivity(enableTrackingIntent);
                        break;
                    case R.id.drawer_login_btn:
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        break;
                    case R.id.drawer_logout_btn:
                        FirebaseAuth.getInstance().signOut();
                        Intent restartIntent = new Intent(MainActivity.this, SlashActivity.class);
                        restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(restartIntent);
                        finish();
                        break;
                    case R.id.drawer_signup_btn:
                        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(registerIntent);
                        break;

                }

                return true;
            }
        });
        toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        mMainProgress = new ProgressDialog(this);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Source = mSearchFrom.getText().toString().toUpperCase();
                String Destination = mSearchTo.getText().toString().toUpperCase();

                if (!TextUtils.isEmpty(Source) && !TextUtils.isEmpty(Destination)) {
                    mMainProgress.setTitle("Searching ");
                    mMainProgress.setMessage("Please wait while searching for Trucks");
                    mMainProgress.setCanceledOnTouchOutside(false);
                    mMainProgress.show();
                    int s=name.indexOf(Source);
                    int d=name.indexOf(Destination);
                    Intent showIntent = new Intent(MainActivity.this, ShowActivity.class);
                    showIntent.putExtra("source", code.get(s));
                    showIntent.putExtra("destination",code.get(d));
                    showIntent.putExtra("sname",name.get(s));
                    showIntent.putExtra("dname",name.get(d));
                    mMainProgress.dismiss();
                    startActivity(showIntent);

                } else {
                    Toast.makeText(MainActivity.this, "Please Input to Search", Toast.LENGTH_LONG).show();
                }

            }
        });

      mCheckBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent checkIntent=new Intent(MainActivity.this,CheckActivity.class);
              checkIntent.putStringArrayListExtra("name",name);
              checkIntent.putStringArrayListExtra("code",code);
              int index=name.indexOf(mCheckAll.getText().toString());
              checkIntent.putExtra("val",code.get(index));
              startActivity(checkIntent);
          }
      });
    }

    private void showSlashActivity()
    {
        String  root= Environment.getExternalStorageDirectory().getAbsolutePath();
        final String path=root+"/TrackMyTrain";
        final File dir=new File(path);
        if(!dir.exists())
        {
            dir.mkdirs();
        }

         storageReference=firebaseStorage.getInstance().getReference();
         reference=storageReference.child("station.txt");
         reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
             @Override
             public void onSuccess(Uri uri) {
                 DownloadManager downloadManager=(DownloadManager)MainActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                 DownloadManager.Request request=new DownloadManager.Request(uri);
                 request.setDestinationInExternalPublicDir("/TrackMyTrain","station.txt");
                 downloadManager.enqueue(request);
                 Intent startIntent = new Intent(getApplicationContext(), StartActivity.class);
                 startActivity(startIntent);

             }
         });

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first", false);
        editor.apply();
    }



    }

