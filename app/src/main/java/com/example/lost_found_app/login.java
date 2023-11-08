package com.example.lost_found_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private DatabaseReference ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://lost-found-app-5b8b9-default-rtdb.firebaseio.com/");
    private EditText t,t1;
    private ProgressBar p;
    private Button b;
    private boolean isLoggingIn = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        p = findViewById(R.id.progressBar2);
        t = findViewById(R.id.email);
        t1 = findViewById(R.id.password);
        b = findViewById(R.id.login);
        p.setVisibility(View.INVISIBLE);
        final TextView regg = findViewById(R.id.reg);
        regg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, register.class);
                startActivity(i);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoggingIn) {
                    isLoggingIn = true;
                    // Disable the login button
                    b.setEnabled(false);
                    // Show the progress bar
                    p.setVisibility(View.VISIBLE);

                    String id = t.getText().toString();
                    String pass = t1.getText().toString();

                    if (id.isEmpty()) {
                        Toast.makeText(login.this, "Please Enter the Email!!", Toast.LENGTH_SHORT).show();
                        resetLoginButtonState();
                        return;
                    } else if (pass.isEmpty()) {
                        Toast.makeText(login.this, "Please Enter the Password!!", Toast.LENGTH_SHORT).show();
                        resetLoginButtonState();
                        return;
                    }

                    ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Check if uid exists
                            String uid = id;
                            if (snapshot.hasChild(id)) {
                                final String p = snapshot.child(id).child("Password").getValue(String.class);
                                final String name = snapshot.child(id).child("Name").getValue(String.class);
                                final String email = snapshot.child(id).child("Email").getValue(String.class);
                                if (p.equals(pass)) {
                                    SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putBoolean("flag", true);
                                    editor.putString("userid", uid);
                                    editor.putString("username", name);
                                    editor.putString("emailadd", email);
                                    editor.apply();
                                    Toast.makeText(login.this, "Login Successful!!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(login.this, MainActivity.class);
                                    i.putExtra("Name", name);
                                    i.putExtra("Email", email);
                                    i.putExtra("Uid", uid);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(login.this, "Incorrect UID or Password!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(login.this, "Please first Register with UID!!", Toast.LENGTH_SHORT).show();
                            }
                            resetLoginButtonState();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            resetLoginButtonState();
                        }
                    });
                }
            }
        });
    }

    private void resetLoginButtonState() {
        // Reset the login button state
        isLoggingIn = false;
        b.setEnabled(true);
        // Hide the progress bar
        p.setVisibility(View.INVISIBLE);
    }


    void regis(){
        Intent i2 = new Intent(this,register.class);
        startActivity(i2);
    }
}