package com.example.rideredirverapplication.Authentication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.rideredirverapplication.Classes.Driver;
import com.example.rideredirverapplication.R;
import com.example.rideredirverapplication.YourApplication;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    EditText email, name, contactNo, password, locationField;
    Button registerBtn, getLocationBtn;
    TextView loginSwitchText;

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;

    ProgressDialog progressDialog;

    FirebaseAuth auth;
    FirebaseDatabase db;

    // Location-related variables
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        imageView1 = findViewById(R.id.i1);
        imageView2 = findViewById(R.id.i2);
        imageView3 = findViewById(R.id.i3);
        imageView4 = findViewById(R.id.i4);
        imageView5 = findViewById(R.id.i5);

        startAnimation(imageView1, 600); // Delay by 100 milliseconds
        startAnimation(imageView2, 700); // Delay by 200 milliseconds
        startAnimation(imageView3, 300); // Delay by 300 milliseconds
        startAnimation(imageView4, 400); // Delay by 400 milliseconds
        startAnimation(imageView5, 500); // Delay by 500 milliseconds


        initComponents();
        attachListeners();
    }

    private void initComponents() {
        Intent in = getIntent();
        String prevEmail = in.getStringExtra("EMAIL");
        name = findViewById(R.id.nameField);
        contactNo = findViewById(R.id.contactNoField);
        email = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        locationField = findViewById(R.id.Location);
        getLocationBtn = findViewById(R.id.getLocationButton);
        registerBtn = findViewById(R.id.registerBtn);
        loginSwitchText = findViewById(R.id.loginSwitchText);

        email.setText(prevEmail);
        email.setSelection(email.getText().length());

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        // Initialize location-related variables
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void attachListeners() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_name = name.getText().toString();
                String txt_contact_no = contactNo.getText().toString();
                String txt_location = locationField.getText().toString(); // Get the location

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegistrationActivity.this, "Email and password can't be blank!", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegistrationActivity.this, "Password should be at least 6 characters long!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setMessage("Signing up...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    registerUser(txt_email, txt_password, txt_name, txt_contact_no, txt_location);
                }
            }
        });

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check and request location permissions if needed
                if (checkLocationPermission()) {
                    // Permissions are granted, proceed to fetch location
                    fetchLocation();
                }
            }
        });

        loginSwitchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    // Check and request location permissions
    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    // Fetch the user's location
    // Fetch the user's location
    private void fetchLocation() {
        try {
            fusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location lastLocation = task.getResult();
                                double latitude = lastLocation.getLatitude();
                                double longitude = lastLocation.getLongitude();

                                // Use Geocoder to fetch the area name based on coordinates
                                Geocoder geocoder = new Geocoder(RegistrationActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    if (!addresses.isEmpty()) {
                                        String areaName = addresses.get(0).getAddressLine(0);
                                        // Create the address without latitude and longitude
                                        String addressWithoutLatLng = areaName.replaceAll("\nLatitude: [0-9.]+\nLongitude: [0-9.]+", "");
                                        // Update the locationField EditText with the modified address
                                        locationField.setText(addressWithoutLatLng);
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "Failed to fetch location.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(RegistrationActivity.this, "Failed to fetch location.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Failed to fetch location.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, fetch location
                fetchLocation();
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerUser(final String email, String password, final String name, final String contact_no, final String location) {
        final YourApplication globalClass = (YourApplication) getApplicationContext();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration is successful
                            final Driver userObj = new Driver(name, email, contact_no, 2, 0, location); // Set user type to owner (2)
                            globalClass.setUserObj(userObj);

                            // Store user details in Firebase Database
                            db.getReference("Users")
                                    .child(auth.getCurrentUser().getUid())
                                    .setValue(userObj)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // User details added successfully
                                                Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                final FirebaseUser user = auth.getCurrentUser();

                                                // Send email verification
                                                user.sendEmailVerification()
                                                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()) {
                                                                    // Verification email sent
                                                                    Toast.makeText(RegistrationActivity.this, "Verification email sent to " + user.getEmail() + "!", Toast.LENGTH_SHORT).show();
                                                                    FirebaseAuth.getInstance().signOut();
                                                                } else {
                                                                    // Failed to send verification email
                                                                    Toast.makeText(RegistrationActivity.this, "Failed to send verification email!", Toast.LENGTH_SHORT).show();
                                                                    FirebaseAuth.getInstance().signOut();
                                                                }
                                                            }
                                                        });
                                                try {
                                                    progressDialog.dismiss();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                                finish();
                                            } else {
                                                // Failed to add User Details
                                                Toast.makeText(RegistrationActivity.this, "Failed to add User Details!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            try {
                                throw task.getException(); // Handle various registration exceptions
                            } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                Toast.makeText(RegistrationActivity.this, "Too Weak Password!", Toast.LENGTH_SHORT).show();
                                Log.d(String.valueOf(RegistrationActivity.this.getClass()), "onComplete: weak_password");
                            } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                Toast.makeText(RegistrationActivity.this, "Malformed_email!", Toast.LENGTH_SHORT).show();
                                Log.d(String.valueOf(RegistrationActivity.this.getClass()), "onComplete: malformed_email");
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Toast.makeText(RegistrationActivity.this, "Email already exists!", Toast.LENGTH_SHORT).show();
                                Log.d(String.valueOf(RegistrationActivity.this.getClass()), "onComplete: exist_email");
                            } catch (Exception e) {
                                Log.d(String.valueOf(RegistrationActivity.this.getClass()), "onComplete: " + e.getMessage());
                                e.printStackTrace();
                                // TODO: Handle other exceptions if needed
                            }
                            progressDialog.dismiss();
                        }
                    }
                });
    }
    private void startAnimation(ImageView imageView, long delay) {
        // To add slide animation to the ImageView with a specified delay
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_animation1);
        animation.setStartOffset(delay);
        imageView.startAnimation(animation);
    }
}
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.rideredirverapplication.Classes.Driver;
//import com.example.rideredirverapplication.MainActivity;
//import com.example.rideredirverapplication.R;
//import com.example.rideredirverapplication.YourApplication;
//import com.example.rideredirverapplication.YourApplication;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
//import com.google.firebase.auth.FirebaseAuthUserCollisionException;
//import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.FirebaseDatabase;


//public class RegistrationActivity extends AppCompatActivity {
//
//    EditText email, name, contactNo, password;
//    Button registerBtn;
//    TextView loginSwitchText;
//    ProgressDialog progressDialog;
//
//    FirebaseAuth auth;
//    FirebaseDatabase db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration);
//
//        initComponents();
//        attachListeners();
//    }
//
//    private void initComponents() {
//        Intent in = getIntent();
//        String prevEmail = in.getStringExtra("EMAIL");
//        name = findViewById(R.id.nameField);
//        contactNo = findViewById(R.id.contactNoField);
//        email = findViewById(R.id.emailField);
//        password = findViewById(R.id.passwordField);
//        registerBtn = findViewById(R.id.registerBtn);
//        loginSwitchText = findViewById(R.id.loginSwitchText);
//
//        email.setText(prevEmail);
//        email.setSelection(email.getText().length());
//
//        auth = FirebaseAuth.getInstance();
//        db = FirebaseDatabase.getInstance();
//
//
//    }
//
//
//
//    private void attachListeners() {
//        registerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String txt_email = email.getText().toString();
//                String txt_password = password.getText().toString();
//                String txt_name = name.getText().toString();
//                String txt_contact_no = contactNo.getText().toString();
//
//                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
//                    Toast.makeText(RegistrationActivity.this, "Email and password can't be blank!", Toast.LENGTH_SHORT).show();
//                } else if (txt_password.length() < 6) {
//                    Toast.makeText(RegistrationActivity.this, "Password should be at least 6 characters long!", Toast.LENGTH_SHORT).show();
//                } else {
//                    progressDialog = new ProgressDialog(RegistrationActivity.this);
//                    progressDialog.setMessage("Signing up...");
//                    progressDialog.setCanceledOnTouchOutside(false);
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//                    registerUser(txt_email, txt_password, txt_name, txt_contact_no);
//                }
//            }
//        });
//
//        loginSwitchText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
//                finish();
//            }
//        });
//    }
//
//    private void registerUser(final String email, String password, final String name, final String contact_no) {
//        final YourApplication globalClass = (YourApplication) getApplicationContext();
//
//        auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            final Driver userObj = new Driver(name, email, contact_no, 2, 0); // Set user type to owner (2)
//                            globalClass.setUserObj(userObj);
//                            db.getReference("Users")
//                                    .child(auth.getCurrentUser().getUid())
//                                    .setValue(userObj).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
//                                                final FirebaseUser user = auth.getCurrentUser();
//                                                user.sendEmailVerification()
//                                                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task task) {
//                                                                if (task.isSuccessful()) {
//                                                                    Toast.makeText(RegistrationActivity.this, "Verification email sent to " + user.getEmail() + "!", Toast.LENGTH_SHORT).show();
//                                                                    FirebaseAuth.getInstance().signOut();
//                                                                } else {
//                                                                    Toast.makeText(RegistrationActivity.this, "Failed to send verification email!", Toast.LENGTH_SHORT).show();
//                                                                    FirebaseAuth.getInstance().signOut();
//                                                                }
//                                                            }
//                                                        });
//                                                try {
//                                                    progressDialog.dismiss();
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
//                                                finish();
//                                            } else {
//                                                Toast.makeText(RegistrationActivity.this, "Failed to add User Details!", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//                        } else {
//                            try {
//                                throw task.getException(); // if user enters wrong email.
//                            } catch (FirebaseAuthWeakPasswordException weakPassword) {
//                                Toast.makeText(RegistrationActivity.this, "Too Weak Password!", Toast.LENGTH_SHORT).show();
//                                Log.d(String.valueOf(RegistrationActivity.this.getClass()), "onComplete: weak_password");
//                            } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
//                                Toast.makeText(RegistrationActivity.this, "Malformed_email!", Toast.LENGTH_SHORT).show();
//                                Log.d(String.valueOf(RegistrationActivity.this.getClass()), "onComplete: malformed_email");
//                            } catch (FirebaseAuthUserCollisionException existEmail) {
//                                Toast.makeText(RegistrationActivity.this, "Email already exists!", Toast.LENGTH_SHORT).show();
//                                Log.d(String.valueOf(RegistrationActivity.this.getClass()), "onComplete: exist_email");
//                            } catch (Exception e) {
//                                Log.d(String.valueOf(RegistrationActivity.this.getClass()), "onComplete: " + e.getMessage());
//                                e.printStackTrace();
//                                // TODO: Handle other exceptions if needed
//                            }
//                            progressDialog.dismiss();
//                        }
//                    }
//                });
//    }
//}
