package com.codepath.apps.destination_vacation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.destination_vacation.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

	public static final String TAG = "LoginActivity";
	private EditText etUsername;
	private EditText etPassword;
	private TextView tvLoginError;
	private Button btnLogin;
	private Button btnSignUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (ParseUser.getCurrentUser() != null) {
			goMainActivity();
		}
		etUsername = findViewById(R.id.etUsername);
		etPassword = findViewById(R.id.etPassword);
		tvLoginError = findViewById(R.id.tvLoginError);
		btnLogin = findViewById(R.id.btnLogin);
		btnSignUp = findViewById(R.id.btnSignUp);

		// Hide incorrect credentials text
		tvLoginError.setVisibility(View.INVISIBLE);

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

				goSignUpActivity();
			}
		});
	}

	void loginUser(String username, String password) {
		Log.i(TAG, "Attempting to login user " + username);
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (e != null) {
					// Show incorrect credentials text
					tvLoginError.setVisibility(View.VISIBLE);
					Log.e(TAG, "Issue with login!", e);
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

	private void goMainActivity() {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();
	}

	private void goSignUpActivity() {
		Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
		startActivity(intent);
	}
}
