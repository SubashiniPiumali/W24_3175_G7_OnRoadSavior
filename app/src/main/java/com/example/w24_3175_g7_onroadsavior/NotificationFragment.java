package com.example.w24_3175_g7_onroadsavior;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.w24_3175_g7_onroadsavior.Database.DBHelper;
import com.example.w24_3175_g7_onroadsavior.Model.Notification;
import com.example.w24_3175_g7_onroadsavior.adapter.NotificationAdapter;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    private ListView mListView;
    private NotificationAdapter mAdapter;
    FirebaseUser currentUser;
    private List<Notification> mNotifications;
    DBHelper DB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        mListView = view.findViewById(R.id.notificationListView);
        mNotifications = new ArrayList<>();
        mAdapter = new NotificationAdapter(getContext(), mNotifications);
        mListView.setAdapter(mAdapter);
        DB = new DBHelper(view.getContext());
        Bundle args = getArguments();

        if (args != null) {
            currentUser = args.getParcelable("CURRENT_USER");
            loadNotifications(currentUser.getUid()); // Load notifications from the database
        }





        return view;
    }

    private void loadNotifications(String userId) {
        // Assuming you have a DBHelper class to handle database operations

        Cursor cursor = DB.getNotificationDetails(userId);

        if(cursor.getCount() !=0) {
            int notificationId = 0;
            String message = null;
            String updateDate = null;
            while (cursor.moveToNext()) {
                notificationId = cursor.getInt(0);
                message = cursor.getString(1);
                updateDate = cursor.getString(2);
                Notification notification = new Notification(message, updateDate);
                mNotifications.add(notification);
            }
        }

        mAdapter.notifyDataSetChanged(); // Notify adapter about the data change
    }
}