package com.example.w24_3175_g7_onroadsavior;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    TextInputLayout regFullName, regUserName, regEmail, regContactNumber, regPassword, regLocation;
    Spinner userTypeSpinner, serviceTypeSpinner;
    RecyclerView placesRecyclerView;
    String fullName, username, email, contactNumber, password, userType, location, serviceType;
    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //widgets
        regFullName = findViewById(R.id.name);
        regUserName = findViewById(R.id.useremail);
        regEmail = findViewById(R.id.email);
        regContactNumber = findViewById(R.id.contactNumber);
        regPassword = findViewById(R.id.password);
        userTypeSpinner = findViewById(R.id.userTypeSpinner);

        regLocation = findViewById(R.id.location);
        serviceTypeSpinner = findViewById(R.id.serviceTypeSpinner);

        Button btnSignUp = findViewById(R.id.btnSignUp);
        Button btnBackToSignIn = findViewById(R.id.btnBackToSignIn);

        //change visibility of service type spinner
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //service requester
                if(position == 0){
                    regLocation.setVisibility(View.GONE);
                    serviceTypeSpinner.setVisibility(View.GONE);
                } else if (position == 1) {
                    regLocation.setVisibility(View.VISIBLE);
                    serviceTypeSpinner.setVisibility(View.VISIBLE);

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //set data in Firebase on button click
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //userInput validation
                if(!isAllUserInputsValid()){
                    return;
                }

                //get values
                fullName = regFullName.getEditText().getText().toString();
                username = regUserName.getEditText().getText().toString();
                email = regEmail.getEditText().getText().toString();
                contactNumber = regContactNumber.getEditText().getText().toString();
                password = regPassword.getEditText().getText().toString();

                int userTypeInd = userTypeSpinner.getSelectedItemPosition();

                if (userTypeInd == 0){
                    userType = "Service Requester";
                    serviceType = "";
                    location = "";
                } else if (userTypeInd == 1){
                    userType = "Service Provider";
                    location = regLocation.getEditText().getText().toString();
                    int serviceTypeInd = serviceTypeSpinner.getSelectedItemPosition();
                    serviceType = getServiceType(serviceTypeInd);
                }

                createAccount(email, password, fullName, username, contactNumber, userType, location, serviceType);
            }

        });


        btnBackToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            }
        });
    }



    //get Service Type
    public static String getServiceType(int pos){

        String serviceType = "";

        switch (pos){
            case 0:
                serviceType = "Tow";
                break;
            case 1:
                serviceType = "Lockout";
                break;
            case 2:
                serviceType = "Fuel Delivery";
                break;
            case 3:
                serviceType = "Tire Change";
                break;
            case 4:
                serviceType = "Jump Start";
                break;
        }

        return serviceType;
    }


    //generate user record in firebase database
    private void generateUserRecord(String uId, String fullName, String username, String email, String contactNumber, String password, String userType, String location, String serviceType){

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("User");
        DatabaseReference userNameRef = reference.child(uId);

        DBHelper DB = new DBHelper(this);

        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists()) {
                    //create new user
                    UserHelperClass user = new UserHelperClass(uId, fullName, username, email, contactNumber, password, userType, location, serviceType);

                    // Save the user data in the appropriate table based on userType
                    reference.child(uId).setValue(user, new CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                            if(error == null) {
                                //add user record to local db
                                DB.addUser(user.getuID(), user.getFullName(), user.getUserName(), user.getEmail(), user.getContactNumber(), user.getUserType());

                                if (user.getUserType().equalsIgnoreCase("Service Provider")) {
                                    //add service provider record
                                    DB.addServiceProvider(user.getuID(), user.getLocation(), user.getServiceType());
                                }
                                Toast.makeText(SignUpActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, "Error occurred while registering user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(SignUpActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);

    }


    //When initializing Activity, check to see if the user is currently signed in
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            redirectToLogin();
        }
    }
    // [END on_start_check_user]


    //Create a new account by passing the new user's email address and password to createUserWithEmailAndPassword
    private void createAccount(String email, String password, String fullName, String username, String contactNumber, String userType, String location, String serviceType) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uId = user.getUid();
                            generateUserRecord(uId, fullName, username, email, contactNumber, password, userType, location, serviceType);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Sign Up failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void redirectToLogin() {

        startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
    }

    private void updateUI(FirebaseUser user) {

    }

    //Validation - Ref: https://www.baeldung.com/
    //user input validation - FullName
    private Boolean validateFullName(){

        String val = regFullName.getEditText().getText().toString();

        if(val.isEmpty()) {
            regFullName.setError("Field cannot be empty");
            return false;
        } else {
            regFullName.setError(null);
            regFullName.setErrorEnabled(false);
            return true;
        }
    }

    //user input validation - Username
    private Boolean validateUsername(){

        String val = regUserName.getEditText().getText().toString();
        String noWhiteSpace = "(?=\\s+$)";

        if(val.isEmpty()) {
            regUserName.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            regUserName.setError("Username too long");
            return false;
        } else if (val.contains(" ")) {
            regUserName.setError("White spaces are not allowed");
            return false;
        } else {
            regUserName.setError(null);
            regUserName.setErrorEnabled(false);
            return true;
        }
    }

    //user input validation - email
    private Boolean validateEmail(){

        String val = regEmail.getEditText().getText().toString();
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        if(val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(regexPattern)){
            regEmail.setError("Invalid Email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    //user input validation - Contact Number
    private Boolean validateContactNumber(){

        String val = regContactNumber.getEditText().getText().toString();
        String contactNumRegex = "^\\d{10}$";

        if(val.isEmpty()) {
            regContactNumber.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(contactNumRegex)){
            regContactNumber.setError("Invalid contact number");
            return false;
        } else {
            regContactNumber.setError(null);
            regContactNumber.setErrorEnabled(false);
            return true;
        }
    }

    //user input validation - Password
    private Boolean validatePassword(){

        String val = regPassword.getEditText().getText().toString();
        String passWordVal = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,20}$";

        /*
            ^: indicates the string’s beginning
            (?=.*[a-z]): makes sure that there is at least one small letter
            (?=.*[A-Z]): needs at least one capital letter
            (?=.*\\d): requires at least one digit
            (?=.*[@#$%^&+=]): provides a guarantee of at least one special symbol
            .{6,20}: imposes the minimum length of 6 characters and the maximum length of 20 characters
            $: terminates the string

            Test PW: Pw@123
         */

        if(val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passWordVal)) {
            regPassword.setError("Password should contain at least one simple letter, one capital letter, one digit, one special symbol and minimum length of 6 characters and the maximum length of 20 characters");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    //validate all user inputs
    private Boolean isAllUserInputsValid(){

        if(validateFullName() && validateUsername() && validateEmail() && validateContactNumber() && validatePassword()){

            return true;
        }

        return false;
    }
}