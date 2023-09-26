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

import com.example.rideredirverapplication.Classes.DLModel;
import com.example.rideredirverapplication.Classes.RCModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VehicleRCActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView VehicleRCFront;
    ImageView VehicleRCBack;

    int requestcode=1;
    Button vehicleRCDetailsBtn;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    Uri kycUriRCFront, kycUriRCBack;
    TextInputEditText vehicleRCNo;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vechile_rcactivity);
        toolbar = findViewById(R.id.toolbarVehicleRC);
        VehicleRCFront =(ImageView) findViewById(R.id.vehicleRCFront);
        VehicleRCBack =(ImageView) findViewById(R.id.vehicleRCBack);
        vehicleRCDetailsBtn = findViewById(R.id.vehicleRCDetailsBtn);
        vehicleRCNo = findViewById(R.id.vehicleRCNo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vehicleRCDetailsBtn.setOnClickListener(v->{
            addDLDetailsFirebase();

        });
    }

    private void addDLDetailsFirebase() {
        if (kycUriRCFront != null && kycUriRCBack != null && !((vehicleRCNo.getText().toString()).isEmpty()) ){
//            String dLFrontName = firebaseAuth.getUid().toString() + "_" + UUID.randomUUID().toString()+"dLFrontPic";
//            String dLBackName = firebaseAuth.getUid().toString() + "_" + UUID.randomUUID().toString()+"dLBackPic";
            firebaseAuth = FirebaseAuth.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference("Driver").child(firebaseAuth.getUid()).child("Kyc Documents");
            StorageReference kycRCFront = storageReference.child("Vehicle Registration Certificate").child("front page");
            kycRCFront.putFile(kycUriRCFront).addOnSuccessListener(taskSnapshot -> {
                kycRCFront.getDownloadUrl().addOnSuccessListener(dlFrontUri->{
                    StorageReference kycRCBack = storageReference.child("Vehicle Registration Certificate").child("back page");
                    kycRCBack.putFile(kycUriRCBack).addOnSuccessListener(taskSnapshot1 -> {
                        kycRCBack.getDownloadUrl().addOnSuccessListener(dlBackUri->{
                            RCModel rCData = new RCModel(dlFrontUri.toString(),dlBackUri.toString(), vehicleRCNo.getText().toString());
                            databaseReference = FirebaseDatabase.getInstance().getReference("Driver").child(firebaseAuth.getUid())
                                    .child("Kyc Documents");
                            databaseReference.child("VehicleRCDetails").setValue(rCData);
                        });
//                        Toast.makeText(this, "Vehicle RC Back Picture is Uploaded", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e->{
                        Toast.makeText(this, "Please upload vehicle RC back picture again", Toast.LENGTH_SHORT).show();
                    });
                });
//                Toast.makeText(this, "Vehicle RC Front Picture is Uploaded", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(VehicleRCActivity.this,DocumentsUploadActivity.class));
                    }
                },200);
            }).addOnFailureListener(e1->{
                Toast.makeText(this, "Please upload vehicle RC front picture again", Toast.LENGTH_SHORT).show();
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

    public void vRCFrontUpload(View view){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,requestcode);
    }
    public void vRCBackUpload(View view){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,2);
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
            VehicleRCFront.setImageURI(uri);
            kycUriRCFront = data.getData();
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
            VehicleRCBack.setImageURI(uri);

            kycUriRCBack = data.getData();
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
//public class VehicleRCActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_vechile_rcactivity);
//    }
//}