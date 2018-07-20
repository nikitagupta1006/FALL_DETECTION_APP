package com.example.nikita.falldetectionapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Contacts extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int mLimit = 5;
    int mCount = 0;
    public static final String nameKey = "nameKey";
    public static final String phoneKey = "phoneKey";
    String sharedPrefPhone = "", sharedPrefName = "";
    ListView mList;
    List<Contact> mNames = new ArrayList<>();
    SharedPreferences mSharedPreferences = null;
    SharedPreferences.Editor mEditor = null;
    public static final String mFile = "SharedPreferences";
    String[] name, phone;
    Contact mSelectedPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mSharedPreferences = getSharedPreferences(mFile, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mList = (ListView)findViewById(R.id.listView);
        mList.setOnItemClickListener(this);
        showContacts();
    }

    private void showContacts()
    {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else {
            mNames = getContactNames();
            name=new String[mNames.size()];
            phone=new String[mNames.size()];

            for(int i = 0; i < mNames.size(); i++) {
                name[i]=mNames.get(i).mContactName;
                phone[i]=mNames.get(i).mContactNumber;

            }
            CustomAdapter2 customAdapter = new CustomAdapter2(this, Arrays.asList(name), Arrays.asList(phone));
            mList.setAdapter(customAdapter);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if(requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showContacts();
            } else {
                Toast.makeText(getApplicationContext(),"Unless you grant permissions, contacts cannot be accessed!",Toast.LENGTH_LONG).show();
            }
        }
    }


    private List<Contact> getContactNames() {

        List<Contact> names = new ArrayList <>();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                if (phoneCursor.moveToNext())
                {
                    String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    names.add(new Contact(name, phoneNumber, id));
                }
                phoneCursor.close();

            }
        }

        cursor.close();
        return names;

    }


    public void addContacts(View view) {

        mCount++;
        if(mCount <= mLimit) {
            sharedPrefName += mSelectedPerson.mContactName + ",";
            sharedPrefPhone += mSelectedPerson.mContactNumber + ",";
            mEditor.putString(nameKey, sharedPrefName);
            mEditor.putString(phoneKey, sharedPrefPhone);
            mEditor.commit();
            Toast.makeText(getApplicationContext(), " " + mSharedPreferences.getString(nameKey, null), Toast.LENGTH_LONG).show();

            String[] mNamesFav = sharedPrefName.split(",");
            String[] mContactFav = sharedPrefPhone.split(",");
            System.out.println("AFTER ADDITION : ");
            for (int i = 0; i < mContactFav.length; i++)
                System.out.println("Name: " + mNamesFav[i] + " ContactNumber: " + mContactFav[i]);

        }
        else
            Toast.makeText(getApplicationContext(), "No further contacts can be added!!", Toast.LENGTH_LONG).show();

    }

    public void removeContacts(View view) {
        Intent removeContacts = new Intent(this, RemoveContacts.class);
        startActivity(removeContacts);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String personName = mNames.get(i).mContactName;
        String contactNumber = mNames.get(i).mContactNumber;
        String id = mNames.get(i).mContactId;
        mSelectedPerson = new Contact(personName, contactNumber, id);

    }
}
