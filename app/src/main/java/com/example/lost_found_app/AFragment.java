package com.example.lost_found_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AFragment extends Fragment {
    private TextView lostCountTextView;
    private TextView foundCountTextView;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);

        lostCountTextView = view.findViewById(R.id.lostcount);
        foundCountTextView = view.findViewById(R.id.foundcount);

        // Get a reference to the "reports" node in your Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("reports");

        // Add a listener to count the lost and found reports
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int lostCount = 0;
                int foundCount = 0;

                for (DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    String status = reportSnapshot.child("status").getValue(String.class);

                    if ("Lost".equals(status)) {
                        lostCount++;
                    } else if ("Found".equals(status)) {
                        foundCount++;
                    }
                }

                // Update the TextViews with the counts
                lostCountTextView.setText(String.valueOf(lostCount));
                foundCountTextView.setText(String.valueOf(foundCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error, if needed
            }
        });

        return view;
    }
}
