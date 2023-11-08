package com.example.lost_found_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //To delay the login screen ie a splash screen code
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // to save the login data into phone storage
                SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
                Boolean check = pref.getBoolean("flag",false);
                String id = pref.getString("userid","null");
                String name = pref.getString("username","null");
                String email = pref.getString("emailadd","null");
                Intent i;
                if(check){
                    i = new Intent(splash.this,MainActivity.class);
                    i.putExtra("Uid",id);
                    i.putExtra("Name",name);
                    i.putExtra("Email",email);
                    startActivity(i);
                    finish();
                }
                else{
                    i = new Intent(splash.this,login.class);
                    startActivity(i);
                    finish();
                }

            }
        },3000);
    }
}