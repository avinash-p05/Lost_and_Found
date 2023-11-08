package com.example.lost_found_app;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lost-found-app-5b8b9-default-rtdb.firebaseio.com/");
    private ProgressBar progress;
    private EditText fName, email, password1, password2, uid;
    private Button registerButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        fName = findViewById(R.id.fname);
        email = findViewById(R.id.email1);
        password1 = findViewById(R.id.p1);
        password2 = findViewById(R.id.p2);
        uid = findViewById(R.id.uid);
        registerButton = findViewById(R.id.r);
        progress = findViewById(R.id.progressBar3);
        progress.setVisibility(View.INVISIBLE);
        final TextView log = findViewById(R.id.loginbtn);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progress.getVisibility() == View.INVISIBLE) {
                    // Ensure that the registration process is not already in progress
                    progress.setVisibility(View.VISIBLE); // Show the progress bar
                    registerButton.setEnabled(false); // Disable the Register button

                    String fn = fName.getText().toString();
                    String id = uid.getText().toString();
                    String userEmail = email.getText().toString();
                    String pass1 = password1.getText().toString();
                    String pass2 = password2.getText().toString();

                    if (fn.isEmpty() || id.isEmpty() || userEmail.isEmpty() || pass1.isEmpty()) {
                        // Handle incomplete input fields
                        Toast.makeText(register.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                        resetRegisterButtonState();
                        return;
                    }

                    if (!pass1.equals(pass2)) {
                        // Handle password mismatch
                        Toast.makeText(register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        resetRegisterButtonState();
                        return;
                    }

                    ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(id)) {
                                // Handle existing user
                                Toast.makeText(register.this, "UID/USN is already Registered!!", Toast.LENGTH_SHORT).show();
                                resetRegisterButtonState();
                            } else {
                                // Register the user
                                ref.child("users").child(id).child("Name").setValue(fn);
                                ref.child("users").child(id).child("Email").setValue(userEmail);
                                ref.child("users").child(id).child("Password").setValue(pass1);

                                Toast.makeText(register.this, "Registered Successfully!!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            resetRegisterButtonState();
                        }
                    });
                }
            }
        });
    }

    private void resetRegisterButtonState() {
        progress.setVisibility(View.INVISIBLE);
        registerButton.setEnabled(true);
    }
}
