package com.example.dell.trackmytrain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SlashActivity extends AppCompatActivity {
private static int SLASH_TIME_OUT=500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                }
            }, SLASH_TIME_OUT);
        }
    }

