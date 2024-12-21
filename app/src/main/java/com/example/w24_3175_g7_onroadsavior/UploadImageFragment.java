package com.example.w24_3175_g7_onroadsavior;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class UploadImageFragment extends Fragment {
    private ProgressBar progressBar;
    private ImageView uploadImage;

    private Button buttonUploadImage;

    Uri image;

    private String imageUrl;

    private Button buttonNextToSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upload_image, container, false);
        progressBar = view.findViewById(R.id.progressbar);
        uploadImage = view.findViewById(R.id.imageViewImageUpload);
        buttonUploadImage = view.findViewById(R.id.btnUploadImage);
        buttonNextToSubmit = view.findViewById(R.id.buttonNextToSubmit);

        Bundle bundle = this.getArguments();
        String breakdownType = bundle.getString("BREAKDOWNTYPE");
        String address = bundle.getString("CURRENTLOCATION");

        uploadImage.setOnClickListener(v -> {chooseCameraOrPhotos();});

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();

                UploadTask uploadTask = storageRef.child("images/" + UUID.randomUUID().toString()).putFile(image);

                uploadTask.addOnFailureListener(e -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Image upload failed! Please try again..", Toast.LENGTH_LONG).show();

                }).addOnSuccessListener(taskSnapshot -> {
                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(getContext(), "Image uploaded Successfully.", Toast.LENGTH_LONG).show();

                    buttonUploadImage.setEnabled(false);

                    imageUrl = taskSnapshot.getStorage().toString();
                });
            }
        });

        buttonNextToSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("BREAKDOWNTYPE", breakdownType);
                result.putString("CURRENTLOCATION",address);
                result.putString("IMAGE_URL", imageUrl);

                Fragment fragment = new BreakdownRequestFragment();
                fragment.setArguments(result);

                replaceFragment(fragment);

            }
        });

        return view;
    }

    private void chooseCameraOrPhotos() {
        final CharSequence[] items = {"Camera", "Photos"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Complete action using");
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Camera")) {
                pickFromCamera();
            } else if (items[item].equals("Photos")) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
        builder.show();
    }

    public void pickFromCamera() {
        ContentValues values = new ContentValues();
        image = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image);

        camera.launch(cameraIntent);
    }

    ActivityResultLauncher<Intent> camera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == -1) {
                    uploadImage.setImageURI(image);
                    buttonUploadImage.setEnabled(true);

                    Log.d("Camera", "Selected URI: " + image);
                } else {
                    Log.d("Camera", "No media selected");
                }
            }
    );

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
            new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    uploadImage.setImageURI(uri);

                    buttonUploadImage.setEnabled(true);
                    image = uri;

                    Log.d("PhotoPicker", "Selected URI: " + uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            }
    );

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}