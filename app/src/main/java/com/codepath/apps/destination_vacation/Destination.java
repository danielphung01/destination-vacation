package com.codepath.apps.destination_vacation;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Destination")
public class Destination extends ParseObject {
    public static final String KEY_USER = "user";
    // the list of favorite places? What type of values should it be? Array or Object?

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

}
