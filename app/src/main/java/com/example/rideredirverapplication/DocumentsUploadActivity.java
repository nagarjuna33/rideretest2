package com.example.rideredirverapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class DocumentsUploadActivity extends AppCompatActivity {
    CardView dl, profile, rc, aadhaar, bank;
    Toolbar toolbar;
    Button submit;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_upload);
        toolbar = findViewById(R.id.toolbarDocumentation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dl=findViewById(R.id.drivingLicence);
        profile=findViewById(R.id.profileInfo);
        rc=findViewById(R.id.vehicleRc);
        aadhaar =findViewById(R.id.adharPan);
        bank=findViewById(R.id.paymentMethod);
        submit=findViewById(R.id.submitalldocuments);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DocumentsUploadActivity.this,SideNavigation.class);
                startActivity(intent);
            }
        });

        dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DrivingLicense.class);
                startActivity(intent);
            }
        });

        rc.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),VehicleRCActivity.class));
        });
        aadhaar.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),AadhaarPanActivity.class));
        });
        bank.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),BankDetailsActivity.class));
        });
        profile.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        });

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
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class DocumentsUploadActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_documents_upload);
//    }
//}