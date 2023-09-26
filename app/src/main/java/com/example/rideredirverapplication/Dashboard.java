package com.example.rideredirverapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

public class Dashboard extends AppCompatActivity  {

    RadioButton twowheeler, threewheeler, fourwheeler;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        twowheeler = findViewById(R.id.i1);
        threewheeler = findViewById(R.id.i2);
        fourwheeler = findViewById(R.id.i3);

        twowheeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the S2 activity when the ride ImageView is clicked
                Intent intent = new Intent(Dashboard.this, DocumentsUploadActivity.class);
                startActivity(intent);
            }
        });

        threewheeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch a different activity when the rental ImageView is clicked
                Intent intent = new Intent(Dashboard.this, DocumentsUploadActivity.class); // Replace AnotherActivity with the actual activity you want to launch
                startActivity(intent);
            }
        });

        fourwheeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch a different activity when the logistics ImageView is clicked
                Intent intent = new Intent(Dashboard.this, DocumentsUploadActivity.class); // Replace YetAnotherActivity with the actual activity you want to launch
                startActivity(intent);
            }
        });


    }

}
