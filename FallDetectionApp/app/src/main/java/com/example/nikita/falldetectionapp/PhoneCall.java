package com.example.nikita.falldetectionapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

public abstract class PhoneCall extends AppCompatActivity implements View.OnClickListener{
    Button mCall;
    List<String>mPhones;
    SharedPreferences sharedPreferences=null;
    String mFile="sharedPreferences";
    String PhoneKey="phoneKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);
        sharedPreferences=getSharedPreferences(mFile,0);
        mCall = (Button)findViewById(R.id.PhoneCall);
        mCall.setOnClickListener(this);
        mPhones = Arrays.asList(sharedPreferences.getString(PhoneKey, null).split(","));

    }

    public void onClick(View view) {
        for (int i = 0; i < mPhones.size(); i++) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+ mPhones.get(i)));

            if (ActivityCompat.checkSelfPermission(PhoneCall.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
    }

}
