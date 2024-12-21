package com.example.w24_3175_g7_onroadsavior;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.w24_3175_g7_onroadsavior.Database.DBHelper;
import com.example.w24_3175_g7_onroadsavior.Model.UserHelperClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentHandler extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    View navHeader;
    FirebaseStorage storage;
    StorageReference storageReference;
    String imageUrl;
    ImageView navMenuProfilePic;
    TextView navMenuProfileName, navMenuProfileEmail;
    Menu navmenu;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    DBHelper DB;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_bar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission( FragmentHandler.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions ( FragmentHandler.this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},  101);
            }
        }

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        DB = new DBHelper(this);
        currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            redirectToLogin();
            return;
        }


        UserHelperClass user = DB.getUserData(currentUser.getUid());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab_emergency);
        setSupportActionBar(toolbar);
        makeNotification();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav){
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                loadNavProfilePic(currentUser.getUid());
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.bringToFront();

        if (savedInstanceState == null) {
            if(user.getUserType().equalsIgnoreCase("Service Provider")) {
                replaceFragment(new ServiceProviderRequestFragment(), currentUser);

            }
            if(user.getUserType().equalsIgnoreCase("Service Requester")){
                replaceFragment(new UserHomeFragment(), currentUser);

            }
           navigationView.setCheckedItem(R.id.nav_home);
        }

        initNavHeader();

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                signOut();
                redirectToLogin();
                return false;
            }
        });

        navigationView.getMenu().findItem(R.id.nav_home).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                if(user.getUserType().equalsIgnoreCase("Service Provider")) {
                    replaceFragment(new ServiceProviderRequestFragment(), currentUser);
                }
                if(user.getUserType().equalsIgnoreCase("Service Requester")){
                    replaceFragment(new UserHomeFragment(), currentUser);
                }
                fab.setVisibility(View.VISIBLE);
                drawerLayout.closeDrawers();
                return  true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragmentHandler.this, EmergencyActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                if(user.getUserType().equalsIgnoreCase("Service Provider")) {
                    replaceFragment(new ServiceProviderRequestFragment(), currentUser);
                }
                if(user.getUserType().equalsIgnoreCase("Service Requester")){
                    replaceFragment(new UserHomeFragment(), currentUser);
                }
                fab.setVisibility(View.VISIBLE);
                return  true;
            }
            if(item.getItemId() == R.id.history){
                if(user.getUserType().equalsIgnoreCase("Service Provider")){
                    replaceFragment(new ProviderRequestsHistoryFragment(), currentUser);
                }
                if(user.getUserType().equalsIgnoreCase("Service Requester")){
                    replaceFragment(new HistroyFragment(), currentUser);
                }
                fab.setVisibility(View.GONE);
                return  true;
            }
            if(item.getItemId() == R.id.notification){
                replaceFragment(new NotificationFragment(), currentUser);
                fab.setVisibility(View.GONE);
                return  true;
            }
            if(item.getItemId() == R.id.profile){
                replaceFragment(new ProfileFragment(), currentUser);
                fab.setVisibility(View.GONE);
                return  true;
            }
            return false;
        });

    }

    private void replaceFragment(Fragment fragment, FirebaseUser currentUser) {

        Bundle args = new Bundle();
        args.putParcelable("CURRENT_USER", currentUser);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

    private void redirectToLogin() {
        Intent intent = new Intent(FragmentHandler.this, LogInActivity.class);
        startActivity(intent);
    }

    public void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        // [END auth_sign_out]
    }

    private void initNavHeader() {
        View headerView = navigationView.getHeaderView(0); // Get the existing header view

        navMenuProfilePic = headerView.findViewById(R.id.navMenuProfilePic);
        navMenuProfileName = headerView.findViewById(R.id.navMenuProfileName);
        navMenuProfileEmail = headerView.findViewById(R.id.navMenuProfileEmail);

        //get user records from remote database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserHelperClass user = snapshot.getValue(UserHelperClass.class);
                    if (user != null) {
                        if (user.getuID().equalsIgnoreCase(currentUser.getUid())) {
                            navMenuProfileName.setText(user.getFullName());
                            navMenuProfileEmail.setText(user.getEmail());
                            loadNavProfilePic(currentUser.getUid());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadNavProfilePic(String uid){
        StorageReference profileImageRef = storageReference.child("profile_images/" +uid+ ".jpg");

        if (navMenuProfilePic != null) {
            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(navMenuProfilePic);
                imageUrl = uri.toString();
            }).addOnFailureListener(exception -> {
                Log.e("UserRequestAcceptFragment", "Failed to load profile image: " + exception.getMessage());
            });
        } else {
            Log.e("UserRequestAcceptFragment", "ImageView is null");
        }
    }
    private void makeNotification() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        Cursor cursor  = DB.getNotificationDetails(currentUser.getUid());
        if(cursor.getCount() !=0){
            int notificationId = 0;
            String message = null;
            while(cursor.moveToNext()){
                notificationId =   cursor.getInt(0);
                message =   cursor.getString(1);
            }

            SharedPreferences sharedPreferences = getSharedPreferences("NotificationPrefs", MODE_PRIVATE);
            boolean notificationShown = sharedPreferences.getBoolean("notificationShown_" + notificationId, false);
            if (notificationShown) {
                return;
            }
            String channelID = "CHANNEL_ID_NOTIFICATION";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channelID);
            builder.setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("OnRoadSavior Notification Title")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Intent intent = new Intent(FragmentHandler.this, FragmentHandler.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("DATA", message);
            intent.putExtra("NOTIFICATIONID", notificationId);
            intent.setAction("OPEN_NOTIFICATION_FRAGMENT");

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    0, intent,PendingIntent.FLAG_MUTABLE);
            builder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
                if (notificationChannel == null) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    notificationChannel = new NotificationChannel (channelID, "Some description", importance);
                    notificationChannel.setLightColor(Color.GREEN);
                    notificationChannel.enableVibration (true);
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }

            notificationManager.notify(notificationId, builder.build());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("notificationShown_" + notificationId, true);
            editor.apply();
            DB.updateNotificationStatus(notificationId, currentDateAndTime);

        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        // Load the profile picture when the fragment is resumed
//        if (imageUrl != null && !imageUrl.isEmpty()) {
//            Picasso.get().load(imageUrl).into(navMenuProfilePic);
//        }
//    }
}