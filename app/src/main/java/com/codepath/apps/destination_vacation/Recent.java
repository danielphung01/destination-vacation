package com.codepath.apps.destination_vacation;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Recent")
public class Recent extends ParseObject {

    public static final String KEY_XID = "xid";
    public static final String KEY_NAME = "name";
    public static final String KEY_CATEGORIES = "categories";
    public static final String KEY_USER = "user";

    public String getXid() {
        return getString(KEY_XID);
    }

    public void setXid(String xid) {
        put(KEY_XID, xid);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getCategories() {
        return getString(KEY_CATEGORIES);
    }

    public void setCategories(String categories) {
        put(KEY_CATEGORIES, categories);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
}
