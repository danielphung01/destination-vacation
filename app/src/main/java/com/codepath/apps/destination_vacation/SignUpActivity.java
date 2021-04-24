package com.codepath.apps.destination_vacation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import okhttp3.Headers;

public class SignUpActivity extends OAuthLoginActionBarActivity<RestClient> {

    public static final String TAG = " SignUpActivity";
    private EditText etUsernameSignUp;
    private EditText etEmailSignUp;
    private EditText etPasswordSignUp;
    private EditText etConfirmPasswordSignUp;
    private Button btnSignUpScreen;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (ParseUser.getCurrentUser() != null) {
            Intent i = new Intent(this, SearchActivity.class);
            startActivity(i);
            finish();
        }
        etUsernameSignUp = findViewById(R.id.etUsernameSignUp);
        etEmailSignUp = findViewById(R.id.etEmailSignUp);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);
        etConfirmPasswordSignUp = findViewById(R.id.etConfirmPasswordSignUp);
        btnSignUpScreen = findViewById(R.id.btnSignUpScreen);
        btnSignIn = findViewById(R.id.btnSignIn);


        // Listener for sign up button
        btnSignUpScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsernameSignUp.getText().toString();
                String email = etEmailSignUp.getText().toString();
                String password = etPasswordSignUp.getText().toString();
                String confirm_password = etConfirmPasswordSignUp.getText().toString();
                loginUser(username, password);
            }
        });

        // Listener for login button
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
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
                Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginFailure(Exception e) {

    }
}
