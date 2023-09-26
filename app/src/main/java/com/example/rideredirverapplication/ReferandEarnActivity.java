package com.example.rideredirverapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ReferandEarnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referandearn);
        Button shareButton = findViewById(R.id.shareButton);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to share the application
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share this App");
                String shareMessage = "I found this cool app. Check it out!\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                // Start the share activity
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
        try {
            TabHost tabHost = findViewById(R.id.tabhost);
            tabHost.setup();
            TabHost.TabSpec spec = tabHost.newTabSpec("Tab one");
            spec.setContent(R.id.REFFERAL);
            spec.setIndicator("REFFERAL");
            tabHost.addTab(spec);
            spec = tabHost.newTabSpec("Tab two");
            spec.setContent(R.id.EARNINGS);
            spec.setIndicator("EARNINGS");
            tabHost.addTab(spec);
        }catch (Exception e){
            Toast.makeText(this, "e.getmessage", Toast.LENGTH_SHORT).show();
        }
    }
}


