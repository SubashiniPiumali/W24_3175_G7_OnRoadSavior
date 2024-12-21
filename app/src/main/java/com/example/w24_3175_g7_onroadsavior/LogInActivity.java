package com.example.w24_3175_g7_onroadsavior;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w24_3175_g7_onroadsavior.Database.DBHelper;
import com.example.w24_3175_g7_onroadsavior.Model.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    FragmentHandler fragmentHandler;

    //Ref: https://firebase.google.com/docs/auth/android/password-auth?_gl=1*1wm236u*_up*MQ..*_ga*MTI0MDk0Mjg4MS4xNzEwNDc3MzA0*_ga_CW55HF8NVT*MTcxMDQ3NzMwNC4xLjAuMTcxMDQ3NzM4OC4wLjAuMA..
    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    TextView txtViewInvalidCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        TextInputLayout txtInputUserEmail = findViewById(R.id.useremail);
        TextInputLayout txtInputPassword = findViewById(R.id.password);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        txtViewInvalidCredentials = findViewById(R.id.txtViewInvalidCredentials);
        txtViewInvalidCredentials.setVisibility(View.GONE);

        DBHelper DB = new DBHelper(this);
        DB.clearEmergencyContact();
        DB.addEmergencyContact();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtInputUserEmail.getEditText().getText().toString();
                String password = txtInputPassword.getEditText().getText().toString();

                if (email.isEmpty()) {
                    txtInputUserEmail.setError("Email cannot be empty");
                    return;
                }

                if (password.isEmpty()) {
                    txtInputPassword.setError("Password cannot be empty");
                    return;
                }

                signIn(email, password);
            }
        });
    }


    //When initializing Activity, check to see if the user is currently signed in
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startApplication();
        }
    }
    // [END on_start_check_user]


    //When a user signs in to the app, pass the user's email address and password to signInWithEmailAndPassword
    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        txtViewInvalidCredentials.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email, password)

                //setting a call back
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            startApplication();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            txtViewInvalidCredentials.setVisibility(View.VISIBLE);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void startApplication() {
        Intent intent = new Intent(LogInActivity.this, FragmentHandler.class);
        startActivity(intent);
    }

}