package com.codepath.apps.destination_vacation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.destination_vacation.LoginActivity;
import com.codepath.apps.destination_vacation.R;
import com.codepath.apps.destination_vacation.RestApplication;
import com.codepath.apps.destination_vacation.RestClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.ParseUser;

import org.json.JSONArray;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    RestClient client;

    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvLocations;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        rvLocations = view.findViewById(R.id.rvLocations);

        // TODO: error here, not running properly, client is still null
        client = RestApplication.getRestClient(getContext());

        // Referencing this code:
        //  client = TwitterApp.getRestClient(this);
        //  tweetDao = ((TwitterApp) getApplicationContext()).getMyDatabase().tweetDao();


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Search button pressed. Location: " + etSearch.getText().toString());
                //populateSearchResults();    // TODO: running this method crashes the app because client is not set up, still null
            }
        });
    }


    private void populateSearchResults() { // TODO: this method should probably take in some parameters: radius size, categories selected, and searched location
        client.getLocations(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess" + json.toString());
                // JSONArray jsonArray = json.jsonArray;
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure" + response, throwable);
            }
        });
    }
}