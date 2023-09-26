package com.example.rideredirverapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rideredirverapplication.Classes.AadhaarPanModel;
import com.example.rideredirverapplication.Classes.RCModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AadhaarPanActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView aadhaarFront;
    ImageView aadhaarBack,panCard;

    int requestcode=1;
    Button aadhaarPanDetailsBtn;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    Uri kycUriAadhaarFront, kycUriAadhaarBack, kycUriPanCard;
    TextInputEditText aadhaarCardNo,panCardNo;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aadhaar_pan);
        toolbar = findViewById(R.id.toolbarAdharpan);
        aadhaarFront =(ImageView) findViewById(R.id.AadhaarFront);
        aadhaarBack =(ImageView) findViewById(R.id.AadhaarBack);
        panCard = findViewById(R.id.PancardImage);
        panCardNo = findViewById(R.id.PanEt);

        aadhaarPanDetailsBtn = findViewById(R.id.aadhaarPanBtn);
        aadhaarCardNo = findViewById(R.id.AadharEt);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aadhaarPanDetailsBtn.setOnClickListener(v->{
            addDLDetailsFirebase();

        });
    }

    private void addDLDetailsFirebase() {
        if (kycUriAadhaarFront != null && kycUriAadhaarBack != null && kycUriPanCard != null && !((panCardNo.getText().toString()).isEmpty())  && !((aadhaarCardNo.getText().toString()).isEmpty()) ){
//            String dLFrontName = firebaseAuth.getUid().toString() + "_" + UUID.randomUUID().toString()+"dLFrontPic";
//            String dLBackName = firebaseAuth.getUid().toString() + "_" + UUID.randomUUID().toString()+"dLBackPic";
            firebaseAuth = FirebaseAuth.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference("Driver").child(firebaseAuth.getUid()).child("Kyc Documents").child("Aadhaar & Pan Details");
            StorageReference kycAadhaarFront = storageReference.child("Aadhaar Card").child("front page");
            kycAadhaarFront.putFile(kycUriAadhaarFront).addOnSuccessListener(taskSnapshot -> {
                kycAadhaarFront.getDownloadUrl().addOnSuccessListener(aadhaarFrontUri->{
                    StorageReference kycAadhaarBack = storageReference.child("Aadhaar Card").child("back page");
                    kycAadhaarBack.putFile(kycUriAadhaarBack).addOnSuccessListener(taskSnapshot1 -> {
                        kycAadhaarBack.getDownloadUrl().addOnSuccessListener(aadhaarBackUri->{
                            StorageReference kycPanCard = storageReference.child("Pan Card");
                            kycPanCard.putFile(kycUriPanCard).addOnSuccessListener(taskSnapshot2 ->{
                                kycPanCard.getDownloadUrl().addOnSuccessListener(panCardUri->{
                                    AadhaarPanModel aadhaarPanData = new AadhaarPanModel(aadhaarFrontUri.toString(),aadhaarBackUri.toString(),panCardUri.toString(), aadhaarCardNo.getText().toString(),panCardNo.getText().toString());
                                    databaseReference = FirebaseDatabase.getInstance().getReference("Driver").child(firebaseAuth.getUid())
                                            .child("Kyc Documents");
                                    databaseReference.child("AadhaarPanDetails").setValue(aadhaarPanData);
//
                                });
                            }).addOnFailureListener(e->{
                                Toast.makeText(this, "Please upload pan card picture again", Toast.LENGTH_SHORT).show();
                            });
                        });
//                        Toast.makeText(this, "Vehicle RC Back Picture is Uploaded", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e1->{
                        Toast.makeText(this, "Please upload aadhaar card back picture again", Toast.LENGTH_SHORT).show();
                    });
                });
//                Toast.makeText(this, "Vehicle RC Front Picture is Uploaded", Toast.LENGTH_SHORT).show();
                //handle the lag
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(AadhaarPanActivity.this,DocumentsUploadActivity.class));
                    }
                },200);
            }).addOnFailureListener(e2->{
                Toast.makeText(this, "Please upload aadhaar card front picture again", Toast.LENGTH_SHORT).show();
            });
            Toast.makeText(this, "Details uploaded successfully", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Upload details", Toast.LENGTH_SHORT).show();
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

    public void aadhaarFrontUpload(View view){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,requestcode);
    }
    public void aadhaarBackUpload(View view){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    public void panCardUpload(View view){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == this.requestcode && resultCode == Activity.RESULT_OK)
        {
            if (data == null){
                return;
            }
            Uri uri=data.getData();
            aadhaarFront.setImageURI(uri);
            kycUriAadhaarFront = data.getData();
//
         /*  firebaseAuth = FirebaseAuth.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference("Test").child(firebaseAuth.getUid()).child("Kyc Documents");
            kycUriDLFront = data.getData();
            if (kycUriDLFront != null){
                StorageReference kycAadhaar = storageReference.child("Aadhaar card");
                kycAadhaar.putFile(kycUriDLFront).addOnSuccessListener(taskSnapshot -> {
                    kycAadhaar.getDownloadUrl().addOnSuccessListener(uriCom->{
                        databaseReference = FirebaseDatabase.getInstance().getReference("Test")
                                .child(firebaseAuth.getUid());
                        databaseReference.child("aadhaar").setValue(kycAadhaar.toString())
                                .addOnCompleteListener(task->{
                                    if (task.isSuccessful()){
                                        Toast.makeText(DrivingLicense.this, "aadhaar saved successfully", Toast.LENGTH_SHORT).show();
                                        dLFront.setImageResource(R.drawable.adhar);
                                    }else {
                                        Toast.makeText(DrivingLicense.this, "Failed to upload aadhaar", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    });

                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "aadhaar is not in correct format", Toast.LENGTH_SHORT).show();
                });
            }else {
                Toast.makeText(this, "Re-upload aadhaar", Toast.LENGTH_SHORT).show();
            }
*/
        }
        if(requestCode == 2 && resultCode == Activity.RESULT_OK)
        {
            if (data == null){
                return;
            }
            Uri uri=data.getData();
            aadhaarBack.setImageURI(uri);

            kycUriAadhaarBack = data.getData();
         /*   firebaseAuth = FirebaseAuth.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference("Test").child(firebaseAuth.getUid()).child("Kyc Documents");
            kycUriDLBack = data.getData();
            if (kycUriDLBack != null){
                StorageReference kycPan = storageReference.child("pan card");
                kycPan.putFile(kycUriDLBack).addOnSuccessListener(taskSnapshot -> {
                    kycPan.getDownloadUrl().addOnSuccessListener(uriCom->{
                        databaseReference = FirebaseDatabase.getInstance().getReference("Test")
                                .child(firebaseAuth.getUid());
                        databaseReference.child("pan card").setValue(kycPan.toString())
                                .addOnCompleteListener(task->{
                                    if (task.isSuccessful()){
                                        Toast.makeText(DrivingLicense.this, "pan card saved successfully", Toast.LENGTH_SHORT).show();
                                        dLBack.setImageResource(R.drawable.pan);
                                    }else {
                                        Toast.makeText(DrivingLicense.this, "Failed to upload pan card", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    });

                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Pan card is not in correct format", Toast.LENGTH_SHORT).show();
                });
            }else {
                Toast.makeText(this, "Re-upload pan card", Toast.LENGTH_SHORT).show();
            }   */
        }
        if(requestCode == 3 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            panCard.setImageURI(uri);
            kycUriPanCard = data.getData();
        }

    }

    //    void openImageChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            selectedImageUri = data.getData();
//            imageView.setImageURI(selectedImageUri);
//        }
//    }

}
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class AadhaarPanActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_aadhaar_pan);
//    }
//}