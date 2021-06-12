package com.codepath.apps.destination_vacation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.destination_vacation.Bookmark;
import com.codepath.apps.destination_vacation.BuildConfig;
import com.codepath.apps.destination_vacation.R;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Objects;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private static final String TAG = "InfoFragment";

    private BottomNavigationView bottomNavigationView;

    // Language is hardcoded as en (english)
    //public static final String URL = "http://api.opentripmap.com/0.1/en/places/xid/_________?apikey=" + BuildConfig.OPENTRIPMAP_API_KEY;
    public static String URL = "https://api.opentripmap.com/0.1/en/places/xid/";

    private TextView tvName;
    private Button btnSave;
    private TextView tvCategories;
    private TextView tvDescription;
    private ImageView ivImage;

    private JSONObject result;
    private String xid = "";
    private String name = "";
    private String categories = "Location Categories";

    int images[] = {R.drawable.ic_baseline_bookmark_border_24, R.drawable.ic_baseline_bookmark_24};
    int i;

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

        // Hide bottom navigation when the info fragment is created
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);

        tvName = view.findViewById(R.id.tvName);
        btnSave = view.findViewById(R.id.btnSave);
        tvCategories = view.findViewById(R.id.tvCategories);
        tvDescription = view.findViewById(R.id.tvDescription);
        ivImage = view.findViewById(R.id.ivImage);

        ParseUser currentUser = ParseUser.getCurrentUser();

        final AsyncHttpClient client = new AsyncHttpClient();

        // Retrieve xid from Bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            xid = bundle.getString("xid");
            categories = bundle.getString("categories");
        }

        // Add xid and apikey to URL for api request
        URL = "https://api.opentripmap.com/0.1/en/places/xid/" + (xid + "?apikey=" + BuildConfig.OPENTRIPMAP_API_KEY);
        Log.i(TAG, URL);

        // Gets jsonObject based on xid
        client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                result = json.jsonObject;
                Log.i(TAG, "Results: " + result.toString());

                // Set views to destination properties
                try {
                    name = result.getString("name");
                    tvName.setText(name);

                    tvCategories.setText("Categories: " + categories);

                    JSONObject jsonWikipediaExtracts = (JSONObject) result.get("wikipedia_extracts");
                    tvDescription.setText(jsonWikipediaExtracts.getString("text"));

                    JSONObject jsonPreview = (JSONObject) result.get("preview");
                    Glide.with(getContext()).load(jsonPreview.getString("source")).into(ivImage);
                    Log.i(TAG, "Image link: " + jsonPreview.getString("source"));
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure, statusCode " + statusCode + ", response:  " + response);
            }
        });


        // Sets the initial bookmark button to the correct image based on whether the user already bookmarked the location
        ParseQuery<Bookmark> query = getQuery(currentUser, xid);
        try {
            if (query.count() == 0) // There are no bookmarks that match the current user and current location
                i = 0;
            else
                i = 1;
        } catch (ParseException e) {
            Log.e(TAG, "Error getting query count", e);
        }
        btnSave.setBackgroundResource(images[i]);


        // Listener for bookmark
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) { // Location was not bookmarked
                    i = 1;

                    // Add bookmark
                    createBookmark(currentUser, xid, name, categories);
                    Toast.makeText(getContext(), "Added to bookmarks", Toast.LENGTH_SHORT).show();
                } else if (i == 1) { // Location was bookmarked
                    i = 0;

                    // Remove bookmark
                    try {
                        removeBookmark(currentUser, xid);
                    } catch (ParseException e) {
                        Log.e(TAG, "Error", e);
                    }

                    Toast.makeText(getContext(), "Removed from bookmarks", Toast.LENGTH_SHORT).show();
                }

                // Change bookmark icon image
                btnSave.setBackgroundResource(images[i]);
            }
        });
    }

    // Show bottom navigation when the info fragment is stopped
    @Override
    public void onStop() {
        super.onStop();
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    // Gets a query of all bookmarks that match the current user and current location
    private ParseQuery<Bookmark> getQuery(ParseUser user, String xid) {
        ParseQuery<Bookmark> query = ParseQuery.getQuery(Bookmark.class);
        query.include(Bookmark.KEY_USER);
        query.whereEqualTo(Bookmark.KEY_USER, user);
        query.whereEqualTo(Bookmark.KEY_XID, xid);

        return query;
    }

    // Creates a bookmark given the current user and the location xid
    private void createBookmark(ParseUser currentUser, String xid, String name, String categories) {
        Bookmark bookmark = new Bookmark();
        bookmark.setUser(currentUser);
        bookmark.setName(name);
        bookmark.setXid(xid);
        bookmark.setCategories(categories);

        bookmark.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error saving bookmark", e);
                    Toast.makeText(getContext(), "Error saving bookmark", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Bookmark saved successfully");

            }
        });
    }

    // Removes a bookmark given the current user and the location xid
    private void removeBookmark(ParseUser currentUser, String xid) throws ParseException {
        ParseQuery<Bookmark> query = getQuery(currentUser, xid);

        query.getFirst().deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error deleting bookmark", e);
                    Toast.makeText(getContext(), "Error saving bookmark", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Bookmark removed successfully");
            }
        });
    }

    // If title in toolbar is clicked
    public static void onTitleClicked() {
        Log.d(TAG, "Title was clicked");
    }
}