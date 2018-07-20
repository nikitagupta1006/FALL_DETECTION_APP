package com.example.nikita.falldetectionapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.nikita.falldetectionapp.Contacts.mFile;
import static com.example.nikita.falldetectionapp.Contacts.nameKey;
import static com.example.nikita.falldetectionapp.Contacts.phoneKey;

public class RemoveContacts extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    Button mRemoveButton;
    Contact mSelectedPerson;
    String[] mPersonNames, mPersonPhone;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    ListView mListView;
    List<String> mName, mPhone;
    HashMap<String, String > mHashmap = new HashMap<>();
    String mRemoveName, mRemoveNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_contacts);
        mRemoveButton = (Button) findViewById(R.id.removeContacts);
        mRemoveButton.setOnClickListener(this);
        mSharedPreferences = getSharedPreferences(mFile, 0);
        mEditor = mSharedPreferences.edit();
        mListView = (ListView) findViewById(R.id.finalList);
        mListView.setOnItemClickListener(this);

        mPersonNames = mSharedPreferences.getString(nameKey, null).split(",");
        mPersonPhone = mSharedPreferences.getString(phoneKey, null).split(",");
        mName = Arrays.asList(mPersonNames);
        mPhone = Arrays.asList(mPersonPhone);
        for(int i = 0; i < mName.size(); i++) {
            mHashmap.put(mName.get(i), mPhone.get(i));
        }

        CustomAdapter2 customAdapter2 = new CustomAdapter2(this, Arrays.asList(mPersonNames), Arrays.asList(mPersonPhone));
        mListView.setAdapter(customAdapter2);
    }

    @Override
    public void onClick(View view)
    {
        mHashmap.remove(mRemoveName);
        String SharedPrefName = "", SharedPrefNumber = "";
        for (String key : mHashmap.keySet()) {
            SharedPrefName += key + ",";
        }

        for (String value : mHashmap.values()) {
            SharedPrefNumber += value + ",";
        }

        mEditor.putString(nameKey, SharedPrefName);
        mEditor.putString(phoneKey, SharedPrefNumber);
        mEditor.commit();
        CustomAdapter2 customAdapter2 = new CustomAdapter2(this, Arrays.asList(SharedPrefName.split(",")),Arrays.asList(SharedPrefNumber.split(",")));
        mListView.setAdapter(customAdapter2);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        mRemoveName = mName.get(i);
        mRemoveNumber = mPhone.get(i);
        Toast.makeText(getApplicationContext(), mRemoveName + " " + mRemoveNumber, Toast.LENGTH_LONG).show();

    }

}
