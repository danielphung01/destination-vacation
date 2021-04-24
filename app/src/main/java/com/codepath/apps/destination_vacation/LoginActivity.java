package com.codepath.apps.destination_vacation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.destination_vacation.models.SampleModel;
import com.codepath.apps.destination_vacation.models.SampleModelDao;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import okhttp3.Headers;

public class LoginActivity extends OAuthLoginActionBarActivity<RestClient> {

	public static final String URL = "https://api.opentripmap.com/0.1/en/places/bbox?lon_min=38.364285&lat_min=59.855685&lon_max=38.372809&lat_max=59.859052&kinds=interesting_places&format=geojson&apikey=" + BuildConfig.OPENTRIPMAP_API_KEY;
	public static final String TAG = "LoginActivity";
	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnSignUp;

	SampleModelDao sampleModelDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (ParseUser.getCurrentUser() != null) {
			goMainActivity();
		}
		etUsername = findViewById(R.id.etUsername);
		etPassword = findViewById(R.id.etPassword);
		btnLogin = findViewById(R.id.btnLogin);
		btnSignUp = findViewById(R.id.btnSignUp);

		// Listener for login button
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "onClick login button");
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();
				loginUser(username, password);
			}
		});

		// Listener for sign up button
		btnSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "onClick signup button");
				// TODO: code for signing up (new user)
				Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);

				startActivity(intent);
			}
		});

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

	void loginUser(String username, String password) {
		Log.i(TAG, "Attempting to login user " + username);
		// TODO: navigate to the main activity if the user has signed in properly
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (e != null) {
					// TODO: better error handling, maybe have text show "Invalid username or password"
					Log.e(TAG, "Issue with login", e);
					return;
				}
				goMainActivity();
				Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
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

	private void goMainActivity() {
		Intent i = new Intent(this, SearchActivity.class);
		startActivity(i);
		finish();
	}
}
