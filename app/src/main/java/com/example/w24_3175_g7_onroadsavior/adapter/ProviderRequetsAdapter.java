package com.example.w24_3175_g7_onroadsavior.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w24_3175_g7_onroadsavior.Interface.ProviderRequestInterface;
import com.example.w24_3175_g7_onroadsavior.Model.RequestDetails;
import com.example.w24_3175_g7_onroadsavior.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProviderRequetsAdapter extends RecyclerView.Adapter<ProviderRequetsAdapter.RequestViewHolder> {

    private final ProviderRequestInterface providerRequestInterface;
    private Context context;
    private ArrayList<RequestDetails> requestDetails = null;
    StorageReference storageReference;
    FirebaseStorage storage;

    public ProviderRequetsAdapter(Context context, ArrayList<RequestDetails> requestDetails, ProviderRequestInterface providerRequestInterface) {
        this.context = context;
        this.requestDetails = requestDetails;
        this.providerRequestInterface = providerRequestInterface;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.service_provider_request, parent, false);
        return new RequestViewHolder(v, providerRequestInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestDetails req = requestDetails.get(position);
        holder.userName.setText(req.getUserName());
        holder.breakDownType.setText(req.getBreakDownType());
        holder.location.setText(req.getLocation());
        if(req.getStatus().equals("Pending")){
            holder.status.setText("New");
            holder.btnCompleted.setEnabled(false);
        }
        if(req.getStatus().equals("Accept")){
            holder.status.setText("Ongoing");
            holder.btnCompleted.setEnabled(true);
        }
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        StorageReference profileImageRef = storageReference.child("profile_images/" +req.getUserId()+ ".jpg");

        // Check if the ImageView is not null before loading the image
        if (holder.imageViewProfilePic != null) {
            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Load the profile image using Picasso
                Picasso.get().load(uri).into(holder.imageViewProfilePic);
            }).addOnFailureListener(exception -> {
                // Handle failure to load profile image
                Log.e("ProfileFragment", "Failed to load profile image: " + exception.getMessage());
            });
        } else {
            Log.e("ProfileFragment", "ImageView is null");
        }

    }

    @Override
    public int getItemCount() {
        return requestDetails.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView  userName, breakDownType,location, status;
        ImageView imageViewProfilePic;
        Button btnCompleted;
        public RequestViewHolder(@NonNull View itemView, ProviderRequestInterface providerRequestInterface) {
            super(itemView);
            userName=itemView.findViewById(R.id.userName);
            status=itemView.findViewById(R.id.status);
            breakDownType=itemView.findViewById(R.id.breakdowntype);
            location=itemView.findViewById(R.id.location);
            imageViewProfilePic = itemView.findViewById(R.id.imageViewProfilePic);
            btnCompleted = itemView.findViewById(R.id.btnComplete);

            btnCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (providerRequestInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            // Call a method in the interface to handle the click event
                            providerRequestInterface.onCompleteClick(pos);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(providerRequestInterface !=null){
                        int  pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            providerRequestInterface.OnItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
