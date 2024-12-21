package com.example.w24_3175_g7_onroadsavior;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w24_3175_g7_onroadsavior.Database.DBHelper;
import com.example.w24_3175_g7_onroadsavior.Model.BreakdownRequestDetails;
import com.example.w24_3175_g7_onroadsavior.adapter.AdapterBreakdownRequestDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HistroyFragment extends Fragment {

    private RecyclerView recyclerViewHistory;
    private AdapterBreakdownRequestDetails adapterBreakdownRequestDetails;
    private List<BreakdownRequestDetails> breakdownRequestDetailsList;
    DBHelper dbHelper;
    String providerId;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_histroy, container, false);
        dbHelper = new DBHelper(view.getContext());
        recyclerViewHistory = view.findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        breakdownRequestDetailsList = new ArrayList<>();
        adapterBreakdownRequestDetails = new AdapterBreakdownRequestDetails(breakdownRequestDetailsList);

        adapterBreakdownRequestDetails = new AdapterBreakdownRequestDetails(breakdownRequestDetailsList, getContext());

        recyclerViewHistory.setAdapter(adapterBreakdownRequestDetails);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userId = user.getUid();
        } else {
            Log.e("Error", "Can't get user Id");
        }

        Cursor cursor = dbHelper.getBreakdownRequestDataForUser(userId);
        if (cursor != null && cursor.moveToFirst()) {
            int createdDateIndex = cursor.getColumnIndex("Created_Date");
            int updatedDateIndex = cursor.getColumnIndex("Updated_Date");
            int userIdIndex = cursor.getColumnIndex("User_ID");
            int providerIdIndex = cursor.getColumnIndex("Provider_ID");
            int breakdownTypeIndex = cursor.getColumnIndex("Breakdown_Type");
            int locationIndex = cursor.getColumnIndex("Location");
            int descriptionIndex = cursor.getColumnIndex("Description");
            int imageIndex = cursor.getColumnIndex("Image");
            int statusIndex = cursor.getColumnIndex("Status");

            do {
                String createdDate = (createdDateIndex != -1) ? cursor.getString(createdDateIndex) : "";
                String updatedDate = (updatedDateIndex != -1) ? cursor.getString(updatedDateIndex) : "";
                String userId = (userIdIndex != -1) ? cursor.getString(userIdIndex) : "";
                providerId = (providerIdIndex != -1) ? cursor.getString(providerIdIndex) : "";
                String breakdownType = (breakdownTypeIndex != -1) ? cursor.getString(breakdownTypeIndex) : "";
                String location = (locationIndex != -1) ? cursor.getString(locationIndex) : "";
                String description = (descriptionIndex != -1) ? cursor.getString(descriptionIndex) : "";
                String image = (imageIndex != -1) ? cursor.getString(imageIndex) : "";
                String status = (statusIndex != -1) ? cursor.getString(statusIndex) : "";
                //String status ="Done";

                String userName = dbHelper.getUserName(userId);
                String providerName = dbHelper.getProviderName(providerId);
                String providerLocation = dbHelper.getProviderLocation(providerId);
                BreakdownRequestDetails req = new BreakdownRequestDetails(
                        createdDate,
                        updatedDate,
                        userId,
                        providerId,
                        userName,
                        providerName,
                        breakdownType,
                        location,
                        description,
                        image,
                        status,
                        providerLocation
                );
                breakdownRequestDetailsList.add(req);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return view;
    }

    public void updateRequestDetails(List<BreakdownRequestDetails> newRequestDetailsList) {
        breakdownRequestDetailsList.clear();
        breakdownRequestDetailsList.addAll(newRequestDetailsList);
        adapterBreakdownRequestDetails.notifyDataSetChanged();
    }
}