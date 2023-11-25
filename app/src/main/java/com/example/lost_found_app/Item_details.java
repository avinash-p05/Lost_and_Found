package com.example.lost_found_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class Item_details extends AppCompatActivity {
    private TextView status, date, user, obj, loc, des, contact;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Retrieve the Report object
        Report report = (Report) getIntent().getSerializableExtra("REPORT_EXTRA");

        // Initialize views
        status = findViewById(R.id.statusTextView2);
        date = findViewById(R.id.dateTextView2);
        user = findViewById(R.id.userNameTextView2);
        obj = findViewById(R.id.objectName);
        loc = findViewById(R.id.locationobj);
        des = findViewById(R.id.descriptionobj);
        contact = findViewById(R.id.contactno);
        img = findViewById(R.id.imageView33);

        // Set data to views
        status.setText("Status: " + report.getReportType());
        date.setText("Date: " + report.getReportDate());
        user.setText("Reported by: "+report.getUsername());
        obj.setText("Object Name: " + report.getObjectName());
        loc.setText("Location: " + report.getLocation());
        des.setText("Description: " + report.getReportDescription());
        contact.setText("Contact: " + report.getContact());

        // Load image using Glide
        Glide.with(this)
                .load(report.getImageUrl())  // Replace with the actual method to get the image URL
                .into(img);
    }
}
