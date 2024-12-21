package com.example.w24_3175_g7_onroadsavior;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w24_3175_g7_onroadsavior.Database.DBHelper;
import com.example.w24_3175_g7_onroadsavior.Model.ServiceProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class BreakdownRequestFragment extends Fragment {

    DBHelper dbHelper;
    String userId;
    String providerId;
    String description;
    String status;
    List<ServiceProvider> nearbyProviders;

    String breakdownType;
    String address;
    String imageUrl;
    EditText editTextDescription;
    EditText editTextSearchProvider;
    String name;
    String providerName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_breakdown_request, container, false);
        dbHelper = new DBHelper(view.getContext());
        Button buttonRequestSubmit = view.findViewById(R.id.buttonRequestSubmit);
        editTextDescription = view.findViewById(R.id.editTextRequestDescription);
        editTextSearchProvider = view.findViewById(R.id.editTextSearchProvider);


        Bundle bundle = this.getArguments();
        breakdownType = bundle.getString("BREAKDOWNTYPE");
        address = bundle.getString("CURRENTLOCATION");
        imageUrl = bundle.getString("IMAGE_URL");

        editTextSearchProvider.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String searchCity = editTextSearchProvider.getText().toString();
                    filterProvidersByCity(searchCity);
                    return true;
                }
                return false;
            }
        });

        buttonRequestSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProviderSelectionDialog();
            }
        });

        return view;
    }

    private void filterProvidersByCity(String city) {
        if (city.isEmpty()) {
            nearbyProviders = dbHelper.getAllProviders();
        } else {
            nearbyProviders = dbHelper.getProvidersByCity(city);
        }
    }

    private void showProviderSelectionDialog() {
        if (nearbyProviders != null && !nearbyProviders.isEmpty()) {
            CharSequence[] providerNames = new CharSequence[nearbyProviders.size()];
            for (int i = 0; i < nearbyProviders.size(); i++) {
                name = nearbyProviders.get(i).getName();
                if (name != null) {
                    providerNames[i] = name;
                } else {
                    providerNames[i] = "Unknown Provider";
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Select Provider");
            builder.setItems(providerNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    providerId = nearbyProviders.get(which).getId();
                    providerName = nearbyProviders.get(which).getName();
                    createBreakdownRequest();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("No Providers Found");
            builder.setMessage("No providers found for the specified city.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void createBreakdownRequest() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());

        String updatedDate ="";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userId = user.getUid();
        } else {
            Log.e("Error", "Can't get user Id");
        }

        description = editTextDescription.getText().toString();

        status = "Pending";

       // dbHelper.addRequest(currentDateAndTime, updatedDate, userId, providerId, breakdownType, address, description, imageUrl, status);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Breakdown Request Details");
        builder.setMessage("Created Date: " + currentDateAndTime + "\n\n" +
                "Updated Date: " + updatedDate + "\n\n" +
                /*"User Name: " + userName + "\n\n" +*/
                "Provider Name: " + providerName + "\n\n" +
                "Breakdown Type: " + breakdownType + "\n\n" +
                "Current Location: " + address + "\n\n" +
                "Description: " + description + "\n\n" +
                "Image: " + imageUrl +"\n\n" +
                "Status: " + status);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.addRequest(currentDateAndTime, updatedDate, userId, providerId, breakdownType, address, description, imageUrl, status);
                Fragment fragment = new HistroyFragment();
                replaceFragment(fragment);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Request cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        /*Fragment fragment = new HistroyFragment();
        replaceFragment(fragment);*/
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}