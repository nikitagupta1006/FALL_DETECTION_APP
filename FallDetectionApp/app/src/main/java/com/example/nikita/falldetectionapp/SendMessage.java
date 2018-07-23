package com.example.nikita.falldetectionapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SendMessage extends AppCompatActivity implements LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    String mFile = "SharedPreferences", phoneKey = "phoneKey";
     String phone;
     Location mLocation;
    SharedPreferences mSharedPreferences = null;
    List<String> mPhones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        mSharedPreferences = getSharedPreferences(mFile, 0);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; }
            mLocation = locationManager.getLastKnownLocation(bestProvider);
        if (mLocation != null) {
            onLocationChanged(mLocation);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

        mPhones = Arrays.asList(mSharedPreferences.getString(phoneKey, null).split(","));
        for(int i = 0; i < mPhones.size(); i++) {
            sendSMSMessage(mPhones.get(i));
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder geocoder = new Geocoder(SendMessage.this, Locale.getDefault());
        try {
            List<Address> addresses;
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address obj = addresses.get(0);
            String name= obj.getAddressLine(0);
            Toast.makeText(getApplicationContext(),""+name,Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void sendSMSMessage(String phoneNo)
    {
         phone = phoneNo;
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {

                SmsManager smsManager = SmsManager.getDefault();
                StringBuffer smsBody = new StringBuffer();
                smsBody.append("http://maps.google.com?q=");
                smsBody.append(mLocation.getLatitude());
                smsBody.append(",");
                smsBody.append(mLocation.getLongitude());
                smsManager.sendTextMessage(phone, null, smsBody.toString(), null, null);
                Toast.makeText(getApplicationContext(), "SMS sent to " + phone,
                        Toast.LENGTH_LONG).show();


            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    StringBuffer smsBody = new StringBuffer();
                    smsBody.append("http://maps.google.com?q=");
                    smsBody.append(mLocation.getLatitude());
                    smsBody.append(",");
                    smsBody.append(mLocation.getLongitude());
                    smsManager.sendTextMessage(phone, null, smsBody.toString(), null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent to " + phone,
                            Toast.LENGTH_LONG).show();

                }
            }
        }

    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
