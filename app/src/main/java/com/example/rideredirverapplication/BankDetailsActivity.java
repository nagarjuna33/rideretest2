package com.example.rideredirverapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.rideredirverapplication.Classes.BankDetailsModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BankDetailsActivity extends AppCompatActivity {
    TextInputEditText accountNo,confirmAccountNo,ifscCode,branchDetails;
    DatabaseReference databaseReference;
    Button bankDetailsBtn;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        toolbar = findViewById(R.id.toolbarBankDetails);
        accountNo = findViewById(R.id.accountNoEt);
        confirmAccountNo = findViewById(R.id.confirmAccountNoEt);
        ifscCode = findViewById(R.id.ifscCodeEt);
        branchDetails = findViewById(R.id.branchDetailsEt);
        bankDetailsBtn = findViewById(R.id.bankDetailBtn);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bankDetailsBtn.setOnClickListener(v->{
            addBankDetailsFirebase();
        });

    }

    private void addBankDetailsFirebase() {
        if (!(accountNo.getText().toString()).isEmpty() && !(confirmAccountNo.getText().toString()).isEmpty()
                && !(ifscCode.getText().toString()).isEmpty() && !(branchDetails.getText().toString()).isEmpty()) {
            if (confirmAccountNo.getText().toString().equals(accountNo.getText().toString())) {

                firebaseAuth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference("Driver").child(firebaseAuth.getUid())
                        .child("Kyc Documents");
                BankDetailsModel bDData = new BankDetailsModel(accountNo.getText().toString(), confirmAccountNo.getText().toString()
                        , ifscCode.getText().toString(), branchDetails.getText().toString());
                databaseReference.child("BankDetails").setValue(bDData).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Details uploaded successfully", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(BankDetailsActivity.this, DocumentsUploadActivity.class));
                        }
                    }, 200);
                });
            }else {
                Toast.makeText(this, "confirm account number must be same as account number", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Details must not empty", Toast.LENGTH_SHORT).show();
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