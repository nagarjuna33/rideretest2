package com.example.rideredirverapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.rideredirverapplication.Classes.ProfileDetailsModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText firstName,lastName,dateOfBirth,emailid;
    Calendar selectedDateCalendar;
    ImageView profileImage;
    Button profileBtn;
    int requestCode = 1;
    RadioGroup radioGroup;
    RadioButton genderRadioButton;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    RadioButton maleRadioButton;
    RadioButton femaleRadioButton;
    DatabaseReference databaseReference;
    Uri kycProfileUri;
    int selectedId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.toolbarProfile);
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        dateOfBirth = findViewById(R.id.dateofbirth);
        emailid = findViewById(R.id.email);
        radioGroup = findViewById(R.id.radioGroup);
        profileImage = findViewById(R.id.profile_image);
        profileBtn = findViewById(R.id.profileBtn);
        selectedId = radioGroup.getCheckedRadioButtonId();
        genderRadioButton = findViewById(selectedId);
        maleRadioButton = findViewById(R.id.male);
        femaleRadioButton = findViewById(R.id.female);

// Add this code inside your onCreate method
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedId = checkedId;
                genderRadioButton = findViewById(selectedId);
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedDateCalendar = Calendar.getInstance();
        updateDateText(selectedDateCalendar.getTime());
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfileDetailsFirebase();

            }
        });

    }

    private void updateDateText(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateOfBirth.setText(dateFormat.format(date));
    }
    public void showDatePicker(View view) {
        int year = selectedDateCalendar.get(Calendar.YEAR);
        int month = selectedDateCalendar.get(Calendar.MONTH);
        int dayOfMonth = selectedDateCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        selectedDateCalendar.set(Calendar.YEAR, year);
                        selectedDateCalendar.set(Calendar.MONTH, month);
                        selectedDateCalendar.set(Calendar.DAY_OF_MONTH, day);

                        // Update the date text in the TextInputEditText
                        updateDateText(selectedDateCalendar.getTime());
                    }
                },
                year, month, dayOfMonth);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void addProfileDetailsFirebase() {
        if (kycProfileUri != null && !((firstName.getText().toString()).isEmpty()) && !((lastName.getText().toString()).isEmpty()) &&
                 !((emailid.getText().toString()).isEmpty()) && !((dateOfBirth.getText().toString()).isEmpty())
                ){
            firebaseAuth = FirebaseAuth.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference("Driver").child(firebaseAuth.getUid()).child("Kyc Documents").child("Profile Details");
            StorageReference profilePic = storageReference.child("profile");
            profilePic.putFile(kycProfileUri).addOnSuccessListener(taskSnapshot -> {
                profilePic.getDownloadUrl().addOnSuccessListener(profileUri->{

                    String gender = "";
                    if (selectedId == R.id.male) {
                        gender = "Male";
                    } else if (selectedId == R.id.female) {
                        gender = "Female";
                    }
                        ProfileDetailsModel profileData = new ProfileDetailsModel(profileUri.toString(),firstName.getText().toString(),lastName.getText().toString()
                                ,dateOfBirth.getText().toString(),emailid.getText().toString(),gender);
                        databaseReference = FirebaseDatabase.getInstance().getReference("Driver").child(firebaseAuth.getUid())
                                .child("Kyc Documents");
                        databaseReference.child("Profile Details").setValue(profileData).addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Details uploaded successfully", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(ProfileActivity.this, DocumentsUploadActivity.class));
                                }
                            }, 200);
                        }).addOnFailureListener(e->{
                            Toast.makeText(this, "Details not uploaded.Please try again after sometime", Toast.LENGTH_SHORT).show();
                        });
                    });
                });
        }else {
            Toast.makeText(this, "Details not uploaded", Toast.LENGTH_SHORT).show();
        }

    }

    public void profilePicUpload(View view){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode && resultCode == RESULT_OK){
            if (data == null){
                return;
            }
            Uri uri = data.getData();
            profileImage.setImageURI(uri);
            kycProfileUri = data.getData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}