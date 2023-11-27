package com.example.lost_found_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.MessageFormat;

public class EFragment extends Fragment {
    private TextView e, n, u;
    private Button b;

    private ProgressBar progress;

    private boolean isLoggingOut = false; // Flag to track if logout is in progress

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e, container, false);
        n = view.findViewById(R.id.fname2);
        e = view.findViewById(R.id.email2);
        u = view.findViewById(R.id.uid2);
        b = view.findViewById(R.id.logout);
        progress = view.findViewById(R.id.progressBar5);
        progress.setVisibility(View.INVISIBLE);
        final TextView hub= view.findViewById(R.id.github);
        final TextView link= view.findViewById(R.id.linkedin);
        Bundle data = getArguments();
        if (data != null) {
            String user = data.getString("uid1");
            String name = data.getString("name1");
            String email = data.getString("email1");
            if (u != null) u.setText(MessageFormat.format("USN/UID - {0}", user));
            if (n != null) n.setText(MessageFormat.format("Name - {0}", name));
            if (e != null) e.setText(MessageFormat.format("Email - {0}", email));
        } else {
            // Handle the case where 'data' is null or missing values
            // You can display an error message or handle it as appropriate for your app.
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoggingOut) {
                    isLoggingOut = true;
                    b.setEnabled(false);
                    progress.setVisibility(View.VISIBLE);

                    // Update the "flag" in SharedPreferences to false
                    SharedPreferences pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("flag", false);
                    editor.apply();

                    // Delay for 3 seconds and then proceed to logout
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Finish the MainActivity
                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).finish();
                            }

                            // Start the LoginActivity
                            Intent i = new Intent(getActivity(), login.class);
                            startActivity(i);
                        }
                    }, 3000); // 3 seconds
                }
            }
        });
        hub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://github.com/avinash-p05");
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://www.linkedin.com/in/avinash-pauskar-00b597244/");
            }
        });

        return view;
    }
    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}
