package com.codepath.apps.destination_vacation;

import com.github.scribejava.core.builder.api.DefaultApi20;

/*
 * OpenTripMap.io API
 *
 * Code based on SlackApi.java from scribejava
 * https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/main/java/com/github/scribejava/apis/SlackApi.java
 */
public class OpenTripMapApi extends DefaultApi20 {

    protected OpenTripMapApi() {
    }

    private static class InstanceHolder {
        private static final OpenTripMapApi INSTANCE = new OpenTripMapApi();
    }

    public static OpenTripMapApi instance() {
        return OpenTripMapApi.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.opentripmap.com/0.1/access";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.opentripmap.com/0.1/authorize";
    }
}
