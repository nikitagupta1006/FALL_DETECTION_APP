package com.example.nikita.falldetectionapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter2 extends BaseAdapter {


    private List<String> mNames, mPhones;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomAdapter2(Context context, List<String> mContactName, List<String> mContactNumber) {
        this.context = context;
        this.mPhones = mContactNumber;
        this.mNames = mContactName;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = layoutInflater.inflate(R.layout.activity_custom_adapter2, viewGroup,false);
        TextView txtName = (TextView)row.findViewById(R.id.PersonName1);
        TextView txtNumber = (TextView)row.findViewById(R.id.Phone1);
        txtName.setText(mNames.get(i));
        txtNumber.setText(mPhones.get(i));
        return row;
    }

}
