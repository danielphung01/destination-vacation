package com.codepath.apps.destination_vacation;

import android.content.Context;

import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.core.builder.api.BaseApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class RestClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = OpenTripMapApi.instance();
	public static final String REST_URL = "https://api.opentripmap.com/0.1"; // Base API URL
	public static final String OPENTRIPMAP_API_KEY = BuildConfig.OPENTRIPMAP_API_KEY;
	public static final String CONSUMER_SECRET = "";

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public RestClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				OPENTRIPMAP_API_KEY,
				CONSUMER_SECRET,
				null,  // OAuth2 scope, null for OAuth1
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}

	// DEFINE METHODS for different API endpoints here

	// We are using: /{lang}/places/radius
	public void getLocations(JsonHttpResponseHandler handler, int radius, String location, String kinds, int limit) { // TODO: might need to switch location for long/lat
		String apiUrl = getApiUrl("/openapi.en.json");
		// Can specify query string params directly or through Request Params
		RequestParams params = new RequestParams();

		params.put("lang", "en");
		params.put("radius", radius);		// maximum distance from selected location
		//params.put("location", location);	// selected location. TODO: may have to convert location to long/lat since "location" might not exist in  /{lang}/places/radius
		params.put("kinds", kinds);			// categories selected (to filter results)
		params.put("limit", limit);			// max number of returned objects

		client.get(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
