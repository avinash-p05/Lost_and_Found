package com.example.lost_found_app;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progress = findViewById(R.id.progressBar6);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check for internet connectivity
                if (isNetworkAvailable()) {
                    // Proceed with the app
                    SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                    Boolean check = pref.getBoolean("flag", false);
                    String id = pref.getString("userid", "null");
                    String name = pref.getString("username", "null");
                    String email = pref.getString("emailadd", "null");
                    Intent i;
                    if (check) {
                        i = new Intent(splash.this, MainActivity.class);
                        i.putExtra("Uid", id);
                        i.putExtra("Name", name);
                        i.putExtra("Email", email);
                        startActivity(i);
                        finish();
                    } else {
                        i = new Intent(splash.this, login.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    // Show a Toast indicating no internet connectivity
                    Toast.makeText(splash.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                    finish(); // You might want to finish the activity or handle it differently based on your use case
                }
            }
        }, 4000); // Increased to 4 seconds for checking internet connectivity
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
