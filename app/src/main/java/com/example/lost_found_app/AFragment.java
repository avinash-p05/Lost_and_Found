package com.example.lost_found_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;


public class AFragment extends Fragment {


    private EditText lost,found;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        lost = view.findViewById(R.id.lostcount);
        found = view.findViewById(R.id.foundcount);
        lost.setText("07");
        found.setText("05");



        return view;
    }
}