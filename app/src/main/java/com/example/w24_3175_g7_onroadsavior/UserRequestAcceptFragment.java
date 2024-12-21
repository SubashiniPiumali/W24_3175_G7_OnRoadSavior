package com.example.w24_3175_g7_onroadsavior;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.w24_3175_g7_onroadsavior.Database.DBHelper;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserRequestAcceptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRequestAcceptFragment extends Fragment {

    StorageReference storageReference;
    FirebaseStorage storage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_user_request_accept, container, false);
        ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.SEND_SMS}, 100);

        DBHelper dbHelper = new DBHelper(v.getContext());
        TextView txtUsername = v.findViewById(R.id.textViewUserName);
        TextView txtLocation = v.findViewById(R.id.textViewLocation);
        TextView txtDescription = v.findViewById(R.id.textViewDescription);
        TextView txtBreakDownType = v.findViewById(R.id.textViewBreakDownType);
        TextView txtCreatedDate = v.findViewById(R.id.textViewCreatedDate);
        TextView txtPhoneNo = v.findViewById(R.id.textViewPhoneNo);
        Button btnAccept = v.findViewById(R.id.buttonAcceptRequest);
        Button btnReject = v.findViewById(R.id.buttonRejectRequest);
        ImageView userPic = v.findViewById(R.id.imageViewUserIcon);
        ImageView userBreakdownPic = v.findViewById(R.id.imageViewBreakDown);
        Bundle bundle = this.getArguments();
        String username = bundle.getString("USERNAME");
        String userId = bundle.getString("USERID");
        String phoneNo = bundle.getString("PHONENO");
        String createdDate = bundle.getString("CREATEDDATE");
        String location = bundle.getString("LOCATION");
        String description = bundle.getString("DESCRIPTION");
        String breakDownType = bundle.getString("BREAKDOWNTYPE");
        String breakDownRequestId = bundle.getString("BREAKDOWNREQUESTID");
        String imageUrl = bundle.getString("IMAGEURL");
        txtUsername.setText(username);
        txtBreakDownType.setText(breakDownType);
        txtLocation.setText(location);
        txtDescription.setText(description);

        txtCreatedDate.setText(createdDate);
        txtPhoneNo.setText(phoneNo);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        StorageReference profileImageRef = storageReference.child("profile_images/" +userId+ ".jpg");

        // Check if the ImageView is not null before loading the image
        if (userPic != null) {
            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Load the profile image using Picasso
                Picasso.get().load(uri).into(userPic);
            }).addOnFailureListener(exception -> {
                // Handle failure to load profile image
                Log.e("UserRequestAcceptFragment", "Failed to load profile image: " + exception.getMessage());
            });
        } else {
            Log.e("UserRequestAcceptFragment", "ImageView is null");
        }

        String[] parts = imageUrl.split("/");
        String imageId = parts[parts.length - 1];
        Log.d("TESTDEMO", imageId);
        if (userBreakdownPic != null) {
            StorageReference breakdownImageRef = storageReference.child("images/" + imageId);
            breakdownImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Load the breakdown image using Picasso
                Picasso.get().load(uri).into(userBreakdownPic);
            }).addOnFailureListener(exception -> {
                // Handle failure to load breakdown image
                Log.e("UserRequestAcceptFragment", "Failed to load breakdown image: " + exception.getMessage());
            });
        } else {
            Log.e("UserRequestAcceptFragment", "Breakdown ImageView is null");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        btnAccept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               dbHelper.acceptRequest(Integer.parseInt(breakDownRequestId), userId, currentDateAndTime);
                btnReject.setEnabled(false);
                String message = "Hi, Accept your request by service provider. Thank you.";
                Activity activity = requireActivity();

                btnReject.setVisibility(View.INVISIBLE);
                Bundle result = new Bundle();;
                result.putString("USERNAME",username );
                result.putString("USERID",userId );
                result.putString("BREAKDOWNTYPE",breakDownType);
                result.putString("PHONENO",phoneNo);
                result.putString("CREATEDDATE",createdDate );
                result.putString("LOCATION",location);
                result.putString("DESCRIPTION",description );
                result.putString("MESSAGE","Accepted" );
                result.putString("ACTION","Accept" );
                result.putString("IMAGEURL", imageUrl);
                if(ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    sendSMS(String.valueOf(phoneNo), v, message);
                } else {
                    ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.SEND_SMS}, 100);
                }
                Fragment userRequestFragment = new UserRequestFragment();
                userRequestFragment.setArguments(result);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager() ;
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, userRequestFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }

        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.rejectRequest(Integer.parseInt(breakDownRequestId), userId, currentDateAndTime);
                btnAccept.setEnabled(false);
                String message = "Hi, Reject your request by provider. Thank you.";

                btnAccept.setVisibility(View.INVISIBLE);
                Bundle result = new Bundle();
                result.putString("USERNAME",username );
                result.putString("USERID",userId );
                result.putString("BREAKDOWNTYPE",breakDownType);
                result.putString("PHONENO",phoneNo);
                result.putString("CREATEDDATE",createdDate );
                result.putString("LOCATION",location);
                result.putString("DESCRIPTION",description );
                result.putString("MESSAGE","Rejected" );
                result.putString("ACTION","Reject" );
                result.putString("IMAGEURL", imageUrl);

                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    sendSMS(String.valueOf(phoneNo), v, message);
                } else {
                    ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.SEND_SMS}, 100);
                }
                Fragment userRequestFragment = new UserRequestFragment();
                userRequestFragment.setArguments(result);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager() ;
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, userRequestFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return  v;
    }
    private void sendSMS(String txtPhoneNo, View v, String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(txtPhoneNo, null, message, null, null);
        Toast.makeText(v.getContext(), "SMS sent successfully", Toast.LENGTH_SHORT).show();
    }
}