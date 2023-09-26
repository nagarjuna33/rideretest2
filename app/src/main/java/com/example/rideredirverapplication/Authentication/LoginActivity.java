package com.example.rideredirverapplication.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rideredirverapplication.Classes.Driver;
import com.example.rideredirverapplication.Dashboard;
import com.example.rideredirverapplication.DocumentsUploadActivity;
import com.example.rideredirverapplication.MainActivity;
import com.example.rideredirverapplication.R;
import com.example.rideredirverapplication.Utils.AppConstants;
import com.example.rideredirverapplication.Utils.BasicUtils;

import com.example.rideredirverapplication.YourApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    private EditText email;
    private EditText password;
    private BasicUtils utils;;
    private Button loginBtn;
    private TextView forgotPasswordText, registerSwitchText;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageView1 = findViewById(R.id.i1);
        imageView2 = findViewById(R.id.i2);
        imageView3 = findViewById(R.id.i3);
        imageView4 = findViewById(R.id.i4);
        imageView5 = findViewById(R.id.i5);

        startAnimation(imageView1, 100); // Delay by 100 milliseconds
        startAnimation(imageView2, 200); // Delay by 200 milliseconds
        startAnimation(imageView3, 300); // Delay by 300 milliseconds
        startAnimation(imageView4, 400); // Delay by 400 milliseconds
        startAnimation(imageView5, 500); // Delay by 500 milliseconds


        utils = new BasicUtils();
        initComponents();
        attachListeners();
        if (!utils.isNetworkAvailable(getApplication())) {
            Toast.makeText(LoginActivity.this, "No Network Available!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initComponents() {
        Intent in = getIntent();
        String prevEmail = in.getStringExtra("EMAIL");
        email = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        loginBtn = findViewById(R.id.loginBtn);
        registerSwitchText = findViewById(R.id.registerSwitchText);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);

        email.setText(prevEmail);
        email.setSelection(email.getText().length());

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
    }

    private void attachListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(LoginActivity.this, "Email can't be blank!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this, "Password can't be blank!", Toast.LENGTH_SHORT).show();
                } else if (utils.isNetworkAvailable(getApplication())) {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Signing-in...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    loginUser(txt_email, txt_password);
                } else {
                    Toast.makeText(LoginActivity.this, "No Network Available!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerSwitchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passEmail = email.getText().toString();
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                if (!passEmail.isEmpty()) {
                    intent.putExtra("EMAIL", passEmail);
                }
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passEmail = email.getText().toString();
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                if (!passEmail.isEmpty()) {
                    intent.putExtra("EMAIL", passEmail);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void loginUser(String email, String password) {
        final YourApplication globalClass = (YourApplication) getApplicationContext();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (!auth.getCurrentUser().isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                        auth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "Verification email sent to " + auth.getCurrentUser().getEmail() + "!", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();
                                            try {
                                                progressDialog.dismiss();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Failed to send verification email!", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();
                                            try {
                                                progressDialog.dismiss();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                    } else {
                        db.getReference("Users").child(auth.getCurrentUser().getUid()).child("isVerified").setValue(1);
                        db.getReference("Users").child(auth.getCurrentUser().getUid()).child("email").setValue(auth.getCurrentUser().getEmail());
                        db.getReference().child("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Driver driver = snapshot.getValue(Driver.class);
                                globalClass.setUserObj(driver);
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                                intent.putExtra("FRAGMENT_NO", 0);
                                try {
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                try {
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } else {
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "Login failed. Check your credentials.", Toast.LENGTH_SHORT).show();
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
