package com.codepath.apps.destination_vacation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.apps.destination_vacation.LoginActivity;
import com.codepath.apps.destination_vacation.R;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button btnLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Listener for logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // log the user out
                ParseUser.logOut();
                Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
                // return to login activity
                goLoginActivity();
            }
        });
    }

    // temporary method
    private void goLoginActivity() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        if (getActivity() != null)
            getActivity().finish();
    }
}