package com.codepath.apps.destination_vacation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.destination_vacation.Bookmark;
import com.codepath.apps.destination_vacation.Recent;
import com.codepath.apps.destination_vacation.activities.LoginActivity;
import com.codepath.apps.destination_vacation.R;
import com.codepath.apps.destination_vacation.adapters.DestinationAdapter;
import com.codepath.apps.destination_vacation.adapters.ViewPagerAdapter;
import com.codepath.apps.destination_vacation.models.Destination;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    private TextView tvUser;
    private ImageView ivProfile;
    private Button btnLogout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerView rvRecentSearches;
    private RecyclerView rvBookmarks;
    private SwipeRefreshLayout swipeContainerRecents;
    private SwipeRefreshLayout swipeContainerBookmarks;

    List<Destination> recentSearches;
    List<Destination> bookmarks;

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
        tvUser = view.findViewById(R.id.tvUser);
        ivProfile = view.findViewById(R.id.ivProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        rvRecentSearches = view.findViewById(R.id.rvRecentSearches);
        rvBookmarks = view.findViewById(R.id.rvBookmarks);
        swipeContainerRecents = view.findViewById(R.id.swipeContainerRecents);
        swipeContainerBookmarks = view.findViewById(R.id.swipeContainerBookmarks);

        ParseUser currentUser = ParseUser.getCurrentUser();

        List<RecyclerView> recyclerViews = new ArrayList<RecyclerView>();
        recyclerViews.add(rvRecentSearches);
        recyclerViews.add(rvBookmarks);

        // Show username at top
        tvUser.setText(currentUser.getUsername());

        // Configure the refreshing colors
        swipeContainerRecents.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeContainerBookmarks.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        // Link the TabLayout to the ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Create the adapter
        //final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), recyclerViews);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();

        // Set the adapter on the view pager
        viewPager.setAdapter(viewPagerAdapter);

        // Load recent searches recycler view
        loadRecents(currentUser);

        // Load bookmarks recycler view
        loadBookmarks(currentUser);

        // Listener for recent searches rv refresher
        swipeContainerRecents.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Code to refresh the list here.
                Log.i(TAG, "refresh recent searches rv");
                loadRecents(currentUser);
            }
        });

        // Listener for bookmarks rv refresher
        swipeContainerBookmarks.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Code to refresh the list here.
                Log.i(TAG, "refresh bookmarks rv");
                loadBookmarks(currentUser);
            }
        });

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

    // Gets a query of all bookmarks that match the current user
    private ParseQuery<Bookmark> getQueryB(ParseUser user) {
        ParseQuery<Bookmark> query = ParseQuery.getQuery(Bookmark.class);
        query.include(Bookmark.KEY_USER);
        query.whereEqualTo(Bookmark.KEY_USER, user);

        return query;
    }

    // Gets a query of all recent searches that match the current user
    private ParseQuery<Recent> getQueryR(ParseUser user) {
        ParseQuery<Recent> query = ParseQuery.getQuery(Recent.class);
        query.include(Recent.KEY_USER);
        query.whereEqualTo(Recent.KEY_USER, user);

        return query;
    }

    // return to log in page
    private void goLoginActivity() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        if (getActivity() != null)
            getActivity().finish();
    }

    // If title in toolbar is clicked
    public static void onTitleClicked() {
        Log.d(TAG, "Title was clicked");
    }

    // Load the recent searches recycler view
    private void loadRecents(ParseUser currentUser) {
        recentSearches = new ArrayList<>();
        // Create the adapter for recents recycler view
        final DestinationAdapter destinationAdapterRecent = new DestinationAdapter(getContext(), recentSearches);
        // Set the adapter on the recycler view
        rvRecentSearches.setAdapter(destinationAdapterRecent);
        // Set a Layout Manager on the recycler view
        LinearLayoutManager layoutManagerRecent = new LinearLayoutManager(getContext());
        rvRecentSearches.setLayoutManager(layoutManagerRecent);

        // Get a query of all of the user's bookmarks
        ParseQuery<Recent> query = getQueryR(currentUser);
        query.findInBackground(new FindCallback<Recent>() {
            @Override
            public void done(List<Recent> recentsTemp, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting recents");
                    return;
                }

                // Add all queried bookmarks into bookmarks list as destinations for rvBookmarks
                for (Recent recent : recentsTemp) {
                    // Log.i(TAG, "Recent location: " + recent.getName());
                    Destination d = new Destination();
                    d.setName(recent.getName());
                    d.setCategories(recent.getCategories());
                    d.setXid(recent.getXid());
                    d.setJustName(true);
                    recentSearches.add(d);
                }
                // Reverse the order of the recentSearches array so that the most recent locations show up at the top
                Collections.reverse(recentSearches);

                rvRecentSearches.post(new Runnable() {
                    @Override
                    public void run() {
                        destinationAdapterRecent.notifyDataSetChanged();
                    }
                });

            }
        });

        swipeContainerRecents.setRefreshing(false);
    }

    // Load the bookmarks recycler view
    private void loadBookmarks(ParseUser currentUser) {
        bookmarks = new ArrayList<>();
        // Create the adapter for bookmarks recycler view
        final DestinationAdapter destinationAdapterBookmark = new DestinationAdapter(getContext(), bookmarks);
        // Set the adapter on the recycler view
        rvBookmarks.setAdapter(destinationAdapterBookmark);
        // Set a Layout Manager on the recycler view
        LinearLayoutManager layoutManagerBookmark = new LinearLayoutManager(getContext());
        rvBookmarks.setLayoutManager(layoutManagerBookmark);

        // Get a query of all of the user's bookmarks
        ParseQuery<Bookmark> query = getQueryB(currentUser);
        query.findInBackground(new FindCallback<Bookmark>() {
            @Override
            public void done(List<Bookmark> bookmarksTemp, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting bookmarks");
                    return;
                }

                // Add all queried bookmarks into bookmarks list as destinations for rvBookmarks
                for (Bookmark bookmark : bookmarksTemp) {
                    // Log.i(TAG, "Bookmarked location:" + bookmark.getName());
                    Destination d = new Destination();
                    d.setName(bookmark.getName());
                    d.setCategories(bookmark.getCategories());
                    d.setXid(bookmark.getXid());
                    d.setJustName(true);
                    bookmarks.add(d);
                }
                rvBookmarks.post(new Runnable() {
                    @Override
                    public void run() {
                        destinationAdapterBookmark.notifyDataSetChanged();
                    }
                });

            }
        });

        swipeContainerBookmarks.setRefreshing(false);
    }
}