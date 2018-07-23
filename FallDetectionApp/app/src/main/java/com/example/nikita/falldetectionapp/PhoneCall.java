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
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class PhoneCall extends AppCompatActivity {
    //Button mCall;
    SharedPreferences sharedPreferences=null;
    String mFile="SharedPreferences";
    String PhoneKey="phoneKey";
    String ContactNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);
        sharedPreferences=getSharedPreferences(mFile,0);
//        mCall = (Button)findViewById(R.id.PhoneCall);
//        mCall.setOnClickListener(this);
        ContactNum =  Arrays.asList(sharedPreferences.getString(PhoneKey, null).split(",")).get(0);

    }
/*
    public void onClick(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ContactNum));
        if (ActivityCompat.checkSelfPermission(PhoneCall.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
*/
    public void makeCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ContactNum));
        if (ActivityCompat.checkSelfPermission(PhoneCall.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
}

