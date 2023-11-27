package com.example.lost_found_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Item_details extends AppCompatActivity {
    private TextView status, date, user, obj, loc, des, contact;
    private ImageView img;
    private Button claim;
    private Report report;
    private ProgressBar progess;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Retrieve the Report object
        report = (Report) getIntent().getSerializableExtra("REPORT_EXTRA");

        // Initialize views
        status = findViewById(R.id.statusTextView2);
        date = findViewById(R.id.dateTextView2);
        user = findViewById(R.id.userNameTextView2);
        obj = findViewById(R.id.objectName);
        loc = findViewById(R.id.locationobj);
        des = findViewById(R.id.descriptionobj);
        contact = findViewById(R.id.contactno);
        img = findViewById(R.id.imageView33);
        claim = findViewById(R.id.claim);
        progess = findViewById(R.id.progressBar8);

        // Check if the report has been claimed
        checkIfReportIsClaimed();
    }

    private void checkIfReportIsClaimed() {
        progess.setVisibility(View.VISIBLE);  // Show progress bar
        String date = report.getReportDate().replace(" ", "_").replace("-", "").replace(":", "");
        DatabaseReference claimedReportsRef = FirebaseDatabase.getInstance().getReference().child("claimed_reports").child(date);

        claimedReportsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progess.setVisibility(View.GONE);  // Hide progress bar
                if (dataSnapshot.exists()) {
                    // Report has been claimed
                    showClaimedByUser();
                } else {
                    // Report has not been claimed
                    setupClaimButton();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                progess.setVisibility(View.GONE);  // Hide progress bar in case of an error
                Toast.makeText(Item_details.this, "Error checking claim status", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showClaimedByUser() {
        progess.setVisibility(View.GONE);
        claim.setVisibility(View.GONE);
        // Display the user who claimed the item using a TextView or another appropriate view
        TextView claimedByTextView = findViewById(R.id.claimedByTextView);
        claimedByTextView.setVisibility(View.VISIBLE);

        // Retrieve the claimed by user ID from the database
        String date = report.getReportDate().replace(" ", "_").replace("-", "").replace(":", "");
        DatabaseReference claimedReportsRef = FirebaseDatabase.getInstance().getReference().child("claimed_reports").child(date);

        claimedReportsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ClaimedReport claimedReport = dataSnapshot.getValue(ClaimedReport.class);
                    if (claimedReport != null) {
                        claimedByTextView.setText("Claimed by: " + claimedReport.getUserId());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(Item_details.this, "Error retrieving claimed by user", Toast.LENGTH_SHORT).show();
            }
        });

        // Load other details from the original report
        loadReportDetails();
    }


    private void setupClaimButton() {
        // Set an OnClickListener for the claim button
        progess.setVisibility(View.GONE);
        claim.setOnClickListener(v -> {
            // Check if the current user is the same as the one who reported the item
            if (!report.getUsername().equals(getCurrentUser())) {
                // Claim the item
                claimItem();

                // Display the user who claimed the item
                showClaimedByUser();
            } else {
                // Alert the user that they cannot claim their own item
                Toast.makeText(Item_details.this, "You cannot claim your own item", Toast.LENGTH_SHORT).show();
            }
        });

        // Load details from the original report
        loadReportDetails();
    }
    private String getCurrentUser() {
        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
        String name = pref.getString("username", "null");
        return name;  // Replace with your logic to get the current user
    }
    private void loadReportDetails() {
        progess.setVisibility(View.GONE);
        // Set data to views
        status.setText("Status: " + report.getReportType());
        date.setText("Date: " + report.getReportDate());
        user.setText("Reported by: " + report.getUsername());
        obj.setText("Object Name: " + report.getObjectName());
        loc.setText("Location: " + report.getLocation());
        des.setText("Description: " + report.getReportDescription());
        contact.setText("Contact: " + report.getContact());

        // Load image using Glide
        Glide.with(this)
                .load(report.getImageUrl())  // Replace with the actual method to get the image URL
                .into(img);
    }
    private void claimItem() {
        // Update the local report object
        report.setClaimed(true);

        // Create a new ClaimedReport instance
        ClaimedReport claimedReport = new ClaimedReport(getCurrentUser(), report.getReportDate());
        String date = report.getReportDate().replace(" ", "_").replace("-", "").replace(":", "");

        // Get the unique key for the claimed report in the database
        DatabaseReference claimedReportsRef = FirebaseDatabase.getInstance().getReference().child("claimed_reports").child(date);

        // Save the claimed report to the database
        claimedReportsRef.setValue(claimedReport)
                .addOnSuccessListener(aVoid -> {
                    // Claimed report saved successfully
                    Toast.makeText(Item_details.this, "Item claimed successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(Item_details.this, "Error claiming item", Toast.LENGTH_SHORT).show();
                });

        // Update the original report's claimed status and user ID in the Firebase database
        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference().child("reports").child(date);
        reportsRef.child("claimed").setValue(true);
        reportsRef.child("claimedByUserId").setValue(getCurrentUser());
    }

}
