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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;
import android.text.InputFilter;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CFragment extends Fragment {
    private Switch lost, found;
    private EditText objectname, description, location,contact;
    private Button sub,choose;
    private String user, username;
    private ProgressBar progressBar;
    private ImageView image;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

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
        image = view.findViewById(R.id.imageView);
        choose = view.findViewById(R.id.chooseimg);
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

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
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
        image.setImageURI(null);
        lost.setChecked(false);
        found.setChecked(false);
    }

    private boolean areFieldsFilled() {
        // Check if the fields are filled, and also check if an image is selected
        return !objectname.getText().toString().isEmpty() &&
                !description.getText().toString().isEmpty() &&
                !location.getText().toString().isEmpty() &&
                !contact.getText().toString().isEmpty() &&
                selectedImageUri != null;
    }

    private void saveDataToFirebase() {
        String status = lost.isChecked() ? "Lost" : "Found";

        // Get the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        // Create a new report with the provided data and the current date
        Report report = new Report(username, status, objectname.getText().toString(), location.getText().toString(), description.getText().toString(), contact.getText().toString(), currentDateAndTime);

        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference().child("reports");

        // Use the date and time as the key for the report
        String reportKey = currentDateAndTime.replace(" ", "_").replace("-", "").replace(":", "");
        DatabaseReference newReportRef = reportsRef.child(reportKey);

        // Set the values for the new report, including the date
        newReportRef.child("status").setValue(report.getReportType());
        newReportRef.child("claimed").setValue(false);
        newReportRef.child("objectname").setValue(report.getObjectName());
        newReportRef.child("description").setValue(report.getReportDescription());
        newReportRef.child("contact").setValue(report.getContact());
        newReportRef.child("location").setValue(report.getLocation());
        newReportRef.child("userId").setValue(user);
        newReportRef.child("username").setValue(report.getUsername());
        newReportRef.child("reportDate").setValue(report.getReportDate());

        // Now, set other properties of the report and save it to the database
        newReportRef.child("claimedByUserId").setValue(null); // Initialize claimedByUserId to null

        if (selectedImageUri != null) {
            // Upload the image to Firebase Storage
            uploadImageToFirebase(selectedImageUri, reportKey);  // Use the key obtained from push()
        }
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            // Set the image URI to the ImageView or perform any other operations with the selected image
            image.setImageURI(selectedImageUri);
        } else {
            // Handle the case where image selection was canceled
            Toast.makeText(requireContext(), "Image selection canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase(Uri imageUri, String reportId) {
        // Access Firebase Storage instance
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images");

        // Generate a unique filename for the image using the reportId
        String filename = "image_" + reportId + ".jpg";
        StorageReference imageRef = storageRef.child(filename);

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    // Get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the image URL directly to the database
                        DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference().child("reports").child(reportId);
                        reportRef.child("imageUrl").setValue(uri.toString());

                        // Clear the input fields only when the image is uploaded successfully
                        clearInputFields();
                        isSubmitting = false;
                        sub.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                    Toast.makeText(requireContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    isSubmitting = false;
                    sub.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }
}
