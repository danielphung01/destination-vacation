package com.codepath.apps.destination_vacation.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Destination {

    String xid;
    String name;
    String categories;

    // Empty constructor needed by the Parceler library
    public Destination() {}

    public Destination(JSONObject jsonObject) throws JSONException {
        xid = jsonObject.getString("xid");
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

    // Returns a given number of destination objects from destinationJsonArray starting from a given index
    public static List<Destination> fromJsonArray(JSONArray destinationJsonArray, int limit, int destinationIndex) throws JSONException {
        List<Destination> destinations = new ArrayList<>();
            for (int i = destinationIndex; i < destinationIndex + limit; i++) {
                destinations.add(new Destination(destinationJsonArray.getJSONObject(i)));
            }

        return destinations;
    }

    public String getXid() { return xid; }

    public String getName() { return name; }

    public String getCategories() { return categories; }
}