package com.codepath.apps.destination_vacation;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Bookmark")
public class Bookmark extends ParseObject {

    public static final String KEY_XID = "xid";
    public static final String KEY_USER = "user";

    public String getXid() {
        return getString(KEY_XID);
    }

    public void setXid(String xid) {
        put(KEY_XID, xid);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
}
