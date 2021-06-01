package com.codepath.apps.destination_vacation.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;

import com.codepath.apps.destination_vacation.BuildConfig;
import com.codepath.apps.destination_vacation.R;
import com.codepath.apps.destination_vacation.adapters.DestinationAdapter;
import com.codepath.apps.destination_vacation.models.Destination;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    // TODO Coordinates and place type are hardcoded
    public static final String URL = "https://api.opentripmap.com/0.1/en/places/bbox?lon_min=-120&lat_min=25&lon_max=0&lat_max=50&kinds=interesting_places&format=json&apikey=" + BuildConfig.OPENTRIPMAP_API_KEY;

    private static NestedScrollView nestedScrollView;
    private EditText etSearch;
    private CheckBox checkBox;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private Button btnSearch;
    private RecyclerView rvDestinations;
    private Button btnLoadMore;

    private JSONArray results;
    private final int limit = 10;
    private int destinationIndex = 0;
    private int page;

    List<Destination> destinations;

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

        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        etSearch = view.findViewById(R.id.etSearch);
        checkBox = view.findViewById(R.id.checkBox);
        checkBox2 = view.findViewById(R.id.checkBox2);
        checkBox3 = view.findViewById(R.id.checkBox3);
        checkBox4 = view.findViewById(R.id.checkBox4);
        btnSearch = view.findViewById(R.id.btnSearch);
        rvDestinations = view.findViewById(R.id.rvDestinations);
        btnLoadMore = view.findViewById(R.id.btnLoadMore);
        destinations = new ArrayList<>();

        // Hide load more button
        btnLoadMore.setVisibility(View.GONE);

        // Create the adapter
        final DestinationAdapter destinationAdapter = new DestinationAdapter(getContext(), destinations);

        // Set the adapter on the recycler view
        rvDestinations.setAdapter(destinationAdapter);

        // Set a Layout Manager on the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvDestinations.setLayoutManager(layoutManager);

        // Add a divider to the recycler view
        rvDestinations.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        final AsyncHttpClient client = new AsyncHttpClient();

        // Set an InputMethodManager to hide the keyboard when the search button is pressed
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        // Load more results button listener
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lower/close keyboard
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                // Calculate page number
                page = destinationIndex / limit + 1;
                Log.i(TAG, "Load more button pressed. Getting page " + page + " of results.");

                try {
                    // Add destination objects to the output ArrayList
                    destinations.addAll(Destination.fromJsonArray(results, limit, destinationIndex));
                    destinationIndex += limit;

                    // Update recycler view
                    rvDestinations.post(new Runnable() {
                        @Override
                        public void run() {
                            destinationAdapter.notifyItemInserted(destinations.size() - 1);
                        }
                    });

                    Log.i(TAG, "Number of places in recycler view: " + destinations.size());

                    // Show load more button if there are places not yet displayed
                    if ((destinationIndex + limit) > results.length())
                        btnLoadMore.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }
        });

        // Search button listener
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lower/close keyboard
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Log.i(TAG, "Search button pressed. Query: " + etSearch.getText().toString());

                client.get(URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        try {
                            results = json.jsonArray;
                            Log.i(TAG, "Getting page 1 of results");
                            Log.i(TAG, "Results: " + results.toString());

                            // Add destination objects to the output ArrayList
                            destinations.addAll(Destination.fromJsonArray(results, limit, destinationIndex));
                            destinationIndex += limit;

                            destinationAdapter.notifyDataSetChanged();
                            Log.i(TAG, "Number of places: " + results.length());
                            Log.i(TAG, "Number of places in recycler view: " + destinations.size());

                            // Show load more button if there are places not yet displayed
                            if ((destinationIndex + limit) < results.length())
                                btnLoadMore.setVisibility(View.VISIBLE);
                        } catch(JSONException e) {
                            Log.e(TAG, "Hit json exception", e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.d(TAG, "onFailure");
                    }
                });
            }
        });
    }

    // If title in toolbar is clicked
    public static void onTitleClicked() {
        Log.d(TAG, "Title was clicked");

        // Scroll to top
        nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
    }
}