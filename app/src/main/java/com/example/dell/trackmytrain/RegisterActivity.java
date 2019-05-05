package com.example.dell.trackmytrain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
private TextInputLayout mDisplayName,mEmail,mPassword;
private Button mCreateBtn;
private FirebaseAuth mAuth;
private Toolbar mToolbar;
private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
       mDisplayName=findViewById(R.id.reg_display_name);
        mEmail=findViewById(R.id.reg_email);
        mPassword=findViewById(R.id.reg_password);
        mCreateBtn=findViewById(R.id.reg_create_btn);
        mToolbar=findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRegProgress=new ProgressDialog(this);
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name=mDisplayName.getEditText().getText().toString();
                String email=mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                if(check(email))
                {

                    if (!TextUtils.isEmpty(display_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        mRegProgress.setTitle("Registering User");
                        mRegProgress.setMessage("Please wait while we create your account !");
                        mRegProgress.setCanceledOnTouchOutside(false);
                        mRegProgress.show();
                        regiter_user(display_name, email, password);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Please enter the credentials to Register", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean check(String email) {
        if (email.endsWith("@gmail.com")) {
            return true;
        } else {
            return false;
        }
    }

    private void regiter_user(String display_name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            mRegProgress.dismiss();
                            Intent mainIntent=new Intent(getApplicationContext(),MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            mRegProgress.dismiss();
                            Toast.makeText(RegisterActivity.this, "Please enter valid Email Id",
                                    Toast.LENGTH_LONG).show();

                        }


                    }
                });

    }
}
