package com.example.w24_3175_g7_onroadsavior.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.w24_3175_g7_onroadsavior.Model.EmergencyContact;
import com.example.w24_3175_g7_onroadsavior.R;

import java.util.ArrayList;

public class AdapterEmergencyContact extends BaseAdapter {
    Context context;
    ArrayList<EmergencyContact> contactList;

    public AdapterEmergencyContact(Context applicationContext, ArrayList<EmergencyContact> contactList) {
        this.context = applicationContext;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.listview_item_emergency_contact, parent, false);
        }

        // get current item to be displayed
        EmergencyContact currentItem = (EmergencyContact) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemName = convertView.findViewById(R.id.textViewEmergencyName);
        TextView textViewItemDescription = convertView.findViewById(R.id.textViewEmergencyPhone);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem.getName());
        textViewItemDescription.setText(currentItem.getPhoneNumber());

        // returns the view for the current row
        return convertView;
    }
}