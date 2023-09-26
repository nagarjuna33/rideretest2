package com.example.rideredirverapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class SideNavigation extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_navigation);

        drawerLayout = findViewById(R.id.my_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

//   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Handle navigation item clicks
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_earnings:
                        // Launch the EarningsActivity
                        Intent earningsIntent = new Intent(SideNavigation.this, EarningsActivity.class);
                        startActivity(earningsIntent);
                        break;
                    case R.id.nav_ridehistory:
                        // Launch the ReferAndEarnActivity
                        Intent referIntent = new Intent(SideNavigation.this, Ridehistory.class);
                        startActivity(referIntent);
                        break;

                    case R.id.nav_refer:
                        // Launch the ReferAndEarnActivity
                        Intent rideIntent = new Intent(SideNavigation.this, ReferandEarnActivity.class);
                        startActivity(rideIntent);
                        break;
                    case R.id.nav_settings:
                        // Launch the SettingsActivity
                        Intent settingsIntent = new Intent(SideNavigation.this, Settings.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.nav_support:
                        // Launch the SupportActivity
                        Intent supportIntent = new Intent(SideNavigation.this, SupportActivity.class);
                        startActivity(supportIntent);
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}