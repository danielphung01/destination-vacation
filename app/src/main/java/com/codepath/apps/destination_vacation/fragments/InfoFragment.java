package com.codepath.apps.destination_vacation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.destination_vacation.LoginActivity;
import com.codepath.apps.destination_vacation.R;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private static final String TAG = "InfoFragment";

    private TextView tvName;
    private TextView tvDescription;
    private Button btnSave;

    int images[] = {R.drawable.ic_baseline_bookmark_border_24, R.drawable.ic_baseline_bookmark_24};
    int i = 0;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String name = "";
        String categories = "";

        tvName = view.findViewById(R.id.tvName);
        tvDescription = view.findViewById(R.id.tvDescription);
        btnSave = view.findViewById(R.id.btnSave);

        // Retrieve destination properties from Bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            categories = bundle.getString("categories");
        }

        // Set TextViews to destination properties
        // TODO Add and/or change setText methods to take different properties as needed
        tvName.setText(name);
        tvDescription.setText("Categories: " + categories);


        // TODO: This should check if the user has this location bookmarked or not and set the button image accordingly.
        i = 0;
        btnSave.setBackgroundResource(images[i]);


        // Listener for bookmark
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) { // Location was not bookmarked
                    i = 1;
                    // Add bookmark
                    Toast.makeText(getContext(), "Added to bookmarks", Toast.LENGTH_SHORT).show();
                } else if (i == 1) {// Location was bookmarked
                    i = 0;
                    // Remove bookmark
                    Toast.makeText(getContext(), "Removed from bookmarks", Toast.LENGTH_SHORT).show();
                }
                btnSave.setBackgroundResource(images[i]);
            }
        });
    }

    // If title in toolbar is clicked
    public static void onTitleClicked() {
        Log.d(TAG, "Title was clicked");
    }
}