package com.example.lost_found_app;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private DatabaseReference databaseReference;
    private ProgressBar progress;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, container, false);
        progress = view.findViewById(R.id.progressBar4);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reportAdapter = new ReportAdapter(new ArrayList<>());
        recyclerView.setAdapter(reportAdapter);

        // Show the progress bar
        progress.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("reports");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Report> reportList = new ArrayList<>();

                for (DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve data from Firebase
                    String username = reportSnapshot.child("username").getValue(String.class);
                    String status = reportSnapshot.child("status").getValue(String.class);
                    String objectname = reportSnapshot.child("objectname").getValue(String.class);
                    String location = reportSnapshot.child("location").getValue(String.class);
                    String description = reportSnapshot.child("description").getValue(String.class);
                    String contact = reportSnapshot.child("contact").getValue(String.class);
                    String date = reportSnapshot.child("reportDate").getValue(String.class);

                    // Create a Report object and add it to the list
                    Report report = new Report(username, status, objectname, location, description,contact, date);
                    reportList.add(report);
                }

                reportAdapter.setReportList(reportList);

                // Hide the progress bar after 3 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.GONE);
                    }
                }, 3000); // 3 seconds
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error, if needed
                progress.setVisibility(View.GONE); // Ensure the progress bar is hidden on error
            }
        });

        return view;
    }
}
