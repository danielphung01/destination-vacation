package com.codepath.apps.destination_vacation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.destination_vacation.BuildConfig;
import com.codepath.apps.destination_vacation.R;
import com.codepath.apps.destination_vacation.adapters.DestinationAdapter;
import com.codepath.apps.destination_vacation.models.Destination;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static androidx.core.content.ContextCompat.getSystemService;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    // TODO Coordinates and place type are hardcoded
    public static final String URL = "https://api.opentripmap.com/0.1/en/places/bbox?lon_min=-120&lat_min=25&lon_max=0&lat_max=50&kinds=interesting_places&format=json&apikey=" + BuildConfig.OPENTRIPMAP_API_KEY;

    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvLocations;

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
        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        rvLocations = view.findViewById(R.id.rvDestinations);
        destinations = new ArrayList<>();

        // Create the adapter
        final DestinationAdapter destinationAdapter = new DestinationAdapter(getContext(), destinations);

        // Set the adapter on the recycler view
        rvLocations.setAdapter(destinationAdapter);

        // Set a Layout Manager on the recycler view
        rvLocations.setLayoutManager(new LinearLayoutManager(getContext()));

        rvLocations.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        final AsyncHttpClient client = new AsyncHttpClient();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Lower/close keyboard
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Log.i(TAG, "Search button pressed. Query: " + etSearch.getText().toString());

                client.get(URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        try {
                            JSONArray results = json.jsonArray;
                            Log.i(TAG, "Results: " + results.toString());
                            destinations.addAll(Destination.fromJsonArray(results));
                            destinationAdapter.notifyDataSetChanged();
                            Log.i(TAG, "Number of results: " + destinations.size());
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
}