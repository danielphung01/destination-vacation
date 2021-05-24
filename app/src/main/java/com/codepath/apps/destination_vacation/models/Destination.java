package com.codepath.apps.destination_vacation.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Destination {

    String name;
    String categories;

    // Empty constructor needed by the Parceler library
    public Destination() {}

    public Destination(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString("name");
        categories = jsonObject.getString("kinds");
    }

    public static List<Destination> fromJsonArray(JSONArray destinationJsonArray) throws JSONException {
        List<Destination> destinations = new ArrayList<>();

        for (int i = 0; i < destinationJsonArray.length(); i++) {
            destinations.add(new Destination(destinationJsonArray.getJSONObject(i)));
        }

        return destinations;
    }

    public String getName() { return name; }

    public String getCategories() { return categories; }
}