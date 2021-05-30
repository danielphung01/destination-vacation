package com.codepath.apps.destination_vacation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.codepath.apps.destination_vacation.fragments.InfoFragment;
import com.codepath.apps.destination_vacation.fragments.ProfileFragment;
import com.codepath.apps.destination_vacation.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    private int currFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_search:
                        Log.i(TAG, "search fragment selected");
                        currFrag = 0;
                        fragment = new SearchFragment();
                        break;
                    case R.id.action_info:
                        Log.i(TAG, "info fragment selected");
                        currFrag = 1;
                        fragment = new InfoFragment();
                        break;
                    case R.id.action_profile:
                        Log.i(TAG, "profile fragment selected");
                        currFrag = 2;
                    default:
                        fragment = new ProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_search);

        // onClickListener for entire toolbar
        /*
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked");
            }
        });
         */

        // onClickListener for the title part of the toolbar
        if (toolbar != null) {
            String title = (String) toolbar.getTitle();
            int childCount = toolbar.getChildCount();

            // Iterates through all child views of the toolbar, finds the textview that contains the title
            for (int i = 0; i < childCount; i++) {
                View view = toolbar.getChildAt(i);
                if (view instanceof TextView && ((TextView) view).getText().equals(title)) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Title clicked");
                            switch (currFrag) {
                                case 0:
                                    SearchFragment tempFrag0 = (SearchFragment) fragmentManager.findFragmentById(R.id.action_search);
                                    tempFrag0.onTitleClicked();
                                    break;
                                case 1:
                                    InfoFragment tempFrag1 = (InfoFragment) fragmentManager.findFragmentById(R.id.action_info);
                                    tempFrag1.onTitleClicked();
                                    break;
                                case 2:
                                    ProfileFragment tempFrag2 = (ProfileFragment) fragmentManager.findFragmentById(R.id.action_profile);
                                    tempFrag2.onTitleClicked();
                                    break;
                                default:
                                    Log.i(TAG, "error while clicking title");
                                    break;
                            }
                        }
                    });
                    break;
                }
            }
        }
    }

    // temporary method
    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}