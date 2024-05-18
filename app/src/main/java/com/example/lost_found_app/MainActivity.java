package com.example.lost_found_app;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnView1;
    private boolean doubleBackToExitPressedOnce = false;
    private DatabaseReference ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://lost-found-app-5b8b9-default-rtdb.firebaseio.com/");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bnView1 = findViewById(R.id.bnView);


        bnView1.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.nav_home){
                    loadFrag(new AFragment(),true);
                }
                else if(id==R.id.nav_messages){
                    loadFrag(new BFragment(),false);
                }
                else if(id==R.id.nav_report){
                    loadFrag(new CFragment(),false);
                }

                else{
                    loadFrag(new EFragment(),false);

                }
                return true;
            }
        });
        bnView1.setSelectedItemId(R.id.nav_home);

    }
    public void loadFrag(Fragment fragment, boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        String Uid = getIntent().getStringExtra("Uid");
        String name = getIntent().getStringExtra("Name");
        String email = getIntent().getStringExtra("Email");
        if(Uid==null){
            SharedPreferences pref =  getSharedPreferences("login",MODE_PRIVATE);
        }
        // Create a Bundle and add data to it
        Bundle data = new Bundle();
        data.putString("uid1", Uid);
        data.putString("name1", name);
        data.putString("email1", email);

        // Set the Bundle to the fragment
        fragment.setArguments(data);

        if (flag)
            ft.add(R.id.container, fragment);
        else
            ft.replace(R.id.container, fragment);

        ft.commit();
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}