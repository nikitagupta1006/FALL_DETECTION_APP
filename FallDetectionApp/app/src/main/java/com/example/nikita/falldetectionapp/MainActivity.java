package com.example.nikita.falldetectionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mSend, mCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSend = (Button)findViewById(R.id.send);
        mCall = (Button)findViewById(R.id.Call);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SendMessage.class);
                startActivity(intent);
            }
        });

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PhoneCall.class);
                startActivity(intent);
            }
        });
    }
    public void FetchContacts(View view) {
        Intent readContacts = new Intent(getApplicationContext(), Contacts.class);
        startActivity(readContacts);
    }

    public void notifyUser(View view) {
  Toast.makeText(getApplicationContext(), "Application has started!! ", Toast.LENGTH_SHORT).show();
    }

}
