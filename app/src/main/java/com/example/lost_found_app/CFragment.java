package com.example.lost_found_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;
import android.text.InputFilter;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CFragment extends Fragment {
    private Switch lost, found;
    private EditText objectname, description, location,contact;
    private Button sub;
    private String user,username;
    private ProgressBar progressBar;

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lost-found-app-5b8b9-default-rtdb.firebaseio.com/");

    private boolean isSubmitting = false;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c, container, false);
        lost = view.findViewById(R.id.switchlost);
        found = view.findViewById(R.id.switchfound);
        objectname = view.findViewById(R.id.objectname);
        location = view.findViewById(R.id.location);
        description = view.findViewById(R.id.description);
        sub = view.findViewById(R.id.submit);
        progressBar = view.findViewById(R.id.progressBar);
        contact = view.findViewById(R.id.contact);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(35);
        description.setFilters(filters);
        objectname.setFilters(filters);
        location.setFilters(filters);
        contact.setFilters(filters);
        Bundle data = getArguments();
        if (data != null) {
            user = data.getString("uid1");
            username = data.getString("name1");
        }

        lost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(requireContext(), "You are reporting a lost!!", Toast.LENGTH_SHORT).show();
                    found.setChecked(false);
                    found.setEnabled(false);
                } else {
                    found.setEnabled(true);
                }
            }
        });

        found.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(requireContext(), "You are reporting a found!!", Toast.LENGTH_SHORT).show();
                    lost.setChecked(false);
                    lost.setEnabled(false);
                } else {
                    lost.setEnabled(true);
                }
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSubmitting) {
                    isSubmitting = true;
                    sub.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    if (lost.isChecked() || found.isChecked()) {
                        if (areFieldsFilled()) {
                            saveDataToFirebase();

                            // Additional feedback to the user
                            Toast.makeText(requireContext(), "Reporting in progress...", Toast.LENGTH_SHORT).show();
                            clearInputFields();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isSubmitting = false;
                                    sub.setEnabled(true);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(requireContext(), "Report has been submitted successfully", Toast.LENGTH_SHORT).show();
                                }
                            }, 4000); // 4 seconds
                        } else {
                            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                            isSubmitting = false;
                        }
                    } else {
                        Toast.makeText(requireContext(), "Please select either 'Lost' or 'Found'", Toast.LENGTH_SHORT).show();
                        isSubmitting = false;
                    }
                }
            }
        });

        return view;
    }

    private void clearInputFields() {
        objectname.setText("");
        description.setText("");
        location.setText("");
        contact.setText("");
        lost.setChecked(false);
        found.setChecked(false);
    }


    private boolean areFieldsFilled() {
        return !objectname.getText().toString().isEmpty() && !description.getText().toString().isEmpty() && !location.getText().toString().isEmpty()&& !contact.getText().toString().isEmpty();
    }

    private void saveDataToFirebase() {
        String status = lost.isChecked() ? "Lost" : "Found";

        // Get the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        // Create a new report with the provided data and the current date
        Report report = new Report(username, status, objectname.getText().toString(), location.getText().toString(), description.getText().toString(),contact.getText().toString(), currentDateAndTime);

        // Initialize a reference to the "reports" node in the database
        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference().child("reports");

        // Push the new report with a unique key
        DatabaseReference newReportRef = reportsRef.push();

        // Set the values for the new report, including the date
        newReportRef.child("status").setValue(report.getReportType());
        newReportRef.child("objectname").setValue(report.getObjectName());
        newReportRef.child("description").setValue(report.getReportDescription());
        newReportRef.child("contact").setValue(report.getContact());
        newReportRef.child("location").setValue(report.getLocation());
        newReportRef.child("userId").setValue(user); // Store the user ID
        newReportRef.child("username").setValue(report.getUsername()); // Store the username
        newReportRef.child("reportDate").setValue(report.getReportDate()); // Store the date

        // Clear the input fields and reset the switches
        clearInputFields();
    }

}



