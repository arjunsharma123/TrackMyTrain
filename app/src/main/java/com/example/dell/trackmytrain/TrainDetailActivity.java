package com.example.dell.trackmytrain;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrainDetailActivity extends AppCompatActivity {
private Toolbar mToolbar;

private TextView tname,tnumber,src,des,tsrc,tdes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_detail);

        mToolbar=findViewById(R.id.train_detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Truck Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String number= getIntent().getStringExtra("tnumber");
        String name=getIntent().getStringExtra("tname");
        String stime=getIntent().getStringExtra("tsrc");
        String dtime=getIntent().getStringExtra("tdes");
        String source=getIntent().getStringExtra("src");
        String destination=getIntent().getStringExtra("des");
        tname=findViewById(R.id.detail_tname);
        tnumber=findViewById(R.id.detail_tnumber);
        tsrc=findViewById(R.id.detail_tsrc);
        tdes=findViewById(R.id.detail_tdes);
        src=findViewById(R.id.detail_src);
        des=findViewById(R.id.detail_des);
        tnumber.setText(number);
        tname.setText(name);
        tsrc.setText(stime);
        tdes.setText(dtime);
        src.setText(source);
        des.setText(destination);
    }


}
