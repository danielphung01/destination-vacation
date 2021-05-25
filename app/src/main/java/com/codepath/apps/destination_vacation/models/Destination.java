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


        // Format categories string to be displayed
        categories = Character.toUpperCase(categories.charAt(0)) + categories.substring(1);
        for (int i = 1; i < categories.length(); i++) {
            switch (categories.charAt(i)) {
                case '_':
                    if (categories.substring(i+1, i+4).compareTo("and") == 0)
                        categories = categories.substring(0, i) + " " +
                                categories.substring(i+1);
                    else
                        categories = categories.substring(0, i) + " " +
                                Character.toUpperCase(categories.charAt(i+1)) +
                                categories.substring(i+2);
                    break;
                case ',':
                    categories = categories.substring(0, i) + ", " +
                            Character.toUpperCase(categories.charAt(i+1)) +
                            categories.substring(i+2);
                    break;
                default:
            }
        }
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