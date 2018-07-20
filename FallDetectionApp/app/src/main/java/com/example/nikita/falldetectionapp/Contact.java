package com.example.nikita.falldetectionapp;

public class Contact {
    public String mContactName, mContactNumber, mContactId;

    Contact(String name, String contactNumber, String contactId) {
        mContactName = name;
        mContactId = contactId;
        mContactNumber = contactNumber;
    }

    Contact(String name, String contactNumber) {
        mContactName = name;
        mContactNumber = contactNumber;
    }


}
