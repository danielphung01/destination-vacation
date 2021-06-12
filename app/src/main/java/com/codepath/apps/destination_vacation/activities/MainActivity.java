package com.codepath.apps.destination_vacation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.codepath.apps.destination_vacation.R;
import com.codepath.apps.destination_vacation.fragments.ProfileFragment;
import com.codepath.apps.destination_vacation.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private final Fragment searchFragment = new SearchFragment();
    private final Fragment profileFragment = new ProfileFragment();
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment activeFragment = searchFragment;

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager.beginTransaction()
                .add(R.id.flContainer, searchFragment, getString(R.string.search))
                .add(R.id.flContainer, profileFragment, getString(R.string.profile)).hide(profileFragment)
                .commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set default bottom navigation selection
        bottomNavigationView.setSelectedItemId(R.id.action_search);

        // onNavigationItemSelectedListener for each item in the bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    Log.i(TAG, "search fragment selected");
                    fragmentManager.beginTransaction().hide(activeFragment).show(searchFragment).commit();
                    activeFragment = searchFragment;
                    return true;
                case R.id.action_profile:
                    Log.i(TAG, "profile fragment selected");
                    fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit();
                    activeFragment = profileFragment;
                    return true;
                default:
                    return false;
            }
        });

        // onClickListener for the title part of the toolbar
        if (toolbar != null) {
            String title = (String) toolbar.getTitle();
            int childCount = toolbar.getChildCount();

            // Iterates through all child views of the toolbar, finds the textview that contains the title
            for (int i = 0; i < childCount; i++) {
                View view = toolbar.getChildAt(i);
                if (view instanceof TextView && ((TextView) view).getText().equals(title)) {
                    view.setOnClickListener(v -> {
                        if (activeFragment.equals(searchFragment)) {
                            SearchFragment.onTitleClicked();
                        } else if (activeFragment.equals(profileFragment)) {
                            ProfileFragment.onTitleClicked();
                        } else {
                            Log.d(TAG, "error while clicking title");
                        }
                    });
                }
            }
        }
    }
}