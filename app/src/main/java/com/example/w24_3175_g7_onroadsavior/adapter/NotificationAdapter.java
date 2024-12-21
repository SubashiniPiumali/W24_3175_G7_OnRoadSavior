package com.example.w24_3175_g7_onroadsavior.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.w24_3175_g7_onroadsavior.Model.Notification;
import com.example.w24_3175_g7_onroadsavior.R;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    private Context mContext;
    private List<Notification> mNotifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        super(context, 0, notifications);
        mContext = context;
        mNotifications = notifications;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);

        Notification currentNotification = mNotifications.get(position);

        TextView message = listItem.findViewById(R.id.message);
        TextView updateDate = listItem.findViewById(R.id.updateDate);
        message.setText(currentNotification.getMessage());
        updateDate.setText(currentNotification.getUpodatedDate());

        // Set other fields if needed

        return listItem;
    }
}