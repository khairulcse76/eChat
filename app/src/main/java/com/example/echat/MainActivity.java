package com.example.echat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        if (getSupportActionBar() !=null){
            getSupportActionBar().setTitle("Home");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_3line);
        }

        ActionBarDrawerToggle DrToggle = new ActionBarDrawerToggle(this,drawerLayout,
                R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(DrToggle);
        DrToggle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.nav_Chat){
                    Toast.makeText(getApplicationContext(), "nnnnnnnnn", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "erreeeee", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });





/*
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.nav_home){
            Intent isetup = new Intent(MainActivity.this,SetupProfileActivity.class);
            startActivity(isetup);
            Toast.makeText(MainActivity.this, "Click to home", Toast.LENGTH_SHORT).show();
        } else if (id==R.id.nav_Chat) {
            Toast.makeText(MainActivity.this, "Click to Chat", Toast.LENGTH_SHORT).show();
        }else if (id==R.id.nav_friend){
            Toast.makeText(MainActivity.this, "Click to Friend", Toast.LENGTH_SHORT).show();
        } else if (id==R.id.nav_find_friend) {
            Toast.makeText(MainActivity.this, "Click to Find Friend", Toast.LENGTH_SHORT).show();
        } else if (id==R.id.nav_profile) {
            Toast.makeText(MainActivity.this, "Click to Profile", Toast.LENGTH_SHORT).show();
        } else if (id==R.id.nav_logout) {
            Toast.makeText(MainActivity.this, "Click to Logout", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this, "dont work", Toast.LENGTH_SHORT).show();
        }
        return true;*/
    }
}