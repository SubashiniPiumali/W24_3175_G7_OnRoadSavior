package com.example.w24_3175_g7_onroadsavior;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w24_3175_g7_onroadsavior.Database.DBHelper;
import com.example.w24_3175_g7_onroadsavior.Model.UserHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;
    //variables
    Animation topAnim, bottomAnim;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        ImageView imgViewLogo = findViewById(R.id.imgViewLogo);
        TextView txtViewAppName = findViewById(R.id.txtViewAppName);
        TextView txtViewTagLine = findViewById(R.id.txtViewTagLine);

        imgViewLogo.setAnimation(topAnim);
        txtViewAppName.setAnimation(bottomAnim);
        txtViewTagLine.setAnimation(bottomAnim);

        mAuth = FirebaseAuth.getInstance();
        //get user records from remote database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        //initialize local db
        DBHelper DB = new DBHelper(this);
        DB.clearUserTable();
        DB.clearServiceProviderTable();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserHelperClass user = snapshot.getValue(UserHelperClass.class);
                    if (user != null) {

                        //add user record
                        DB.addUser(user.getuID(), user.getFullName(), user.getUserName(), user.getEmail(), user.getContactNumber(), user.getUserType());

                        if (user.getUserType().equalsIgnoreCase("Service Provider")) {
                            //add service provider record
                            DB.addServiceProvider(user.getuID(), user.getLocation(), user.getServiceType());
                        }
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, LogInActivity.class);

                        Pair[] pairs = new Pair[2];
                        pairs[0] = new Pair<View,String>(imgViewLogo, "logo_image");
                        pairs[1] = new Pair<View, String>(txtViewAppName, "app_name");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
                        startActivity(intent, options.toBundle());
                    }
                }, SPLASH_SCREEN);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read user data from Firebase.", error.toException());
                Toast.makeText(SplashActivity.this, "Failed to read user data from Firebase.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}