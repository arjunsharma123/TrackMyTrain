package com.example.dell.trackmytrain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {
private Button mRegBtn;
private Button mLogBtn;
private ImageView mSkipBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mRegBtn=findViewById(R.id.start_reg_btn);
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(reg_intent);
                finish();
            }
        });
        mLogBtn=findViewById(R.id.start_login_btn);
        mLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log_intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(log_intent);
                finish();
            }
        });
        mSkipBtn=findViewById(R.id.start_skip);
        mSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainIntent);
                 finish();
            }
        });

    }
}
