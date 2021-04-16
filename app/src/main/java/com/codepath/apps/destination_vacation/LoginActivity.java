package com.codepath.apps.destination_vacation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.destination_vacation.models.SampleModel;
import com.codepath.apps.destination_vacation.models.SampleModelDao;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthLoginActionBarActivity;

import org.json.JSONObject;

import okhttp3.Headers;

public class LoginActivity extends OAuthLoginActionBarActivity<RestClient> {

	public static final String URL = "http://api.opentripmap.com/0.1/en/places/bbox?lon_min=38.364285&lat_min=59.855685&lon_max=38.372809&lat_max=59.859052&kinds=interesting_places&format=geojson&apikey=5ae2e3f221c38a28845f05b63fec109c3dd79adf94345801d2cf0a37";
	public static final String TAG = "LoginActivity";

	SampleModelDao sampleModelDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(URL, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Headers headers, JSON json) {
				Log.d(TAG, "onSuccess");
			}

			@Override
			public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
				Log.d(TAG, "onFailure");
			}
		});

		final SampleModel sampleModel = new SampleModel();
		sampleModel.setName("Destination Vacation");

		sampleModelDao = ((RestApplication) getApplicationContext()).getMyDatabase().sampleModelDao();

		AsyncTask.execute(new Runnable() {
			@Override
			public void run() {
				sampleModelDao.insertModel(sampleModel);
			}
		});
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
//		 Intent i = new Intent(this, MainActivity.class);
//		 startActivity(i);
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
