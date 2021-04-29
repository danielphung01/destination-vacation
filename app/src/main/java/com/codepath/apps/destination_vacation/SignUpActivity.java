package com.codepath.apps.destination_vacation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";
    private EditText etUsernameSignUp;
    private EditText etEmailSignUp;
    private EditText etPasswordSignUp;
    private EditText etConfirmPasswordSignUp;
    private TextView tvSignUpError;
    private Button btnSignUpScreen;
    private Button btnExistingAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (ParseUser.getCurrentUser() != null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        etUsernameSignUp = findViewById(R.id.etUsernameSignUp);
        etEmailSignUp = findViewById(R.id.etEmailSignUp);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);
        etConfirmPasswordSignUp = findViewById(R.id.etConfirmPasswordSignUp);
        tvSignUpError = findViewById(R.id.tvSignUpError);
        btnSignUpScreen = findViewById(R.id.btnSignUpScreen);
        btnExistingAccount = findViewById(R.id.btnExistingAccount);

        // Set initial incorrect credentials text to blank
        tvSignUpError.setText("");

        // Listener for sign up button
        btnSignUpScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");
                String username = etUsernameSignUp.getText().toString();
                String email = etEmailSignUp.getText().toString();
                String password = etPasswordSignUp.getText().toString();
                String confirm_password = etConfirmPasswordSignUp.getText().toString();

                // Check for valid username/email and password
                // Show incorrect credentials text if any credentials are invalid
                if (!username.isEmpty()) {
                    if (!email.isEmpty())
                    {
                        if (email.contains("@") && email.contains(".") &&
                                (email.indexOf('.') - email.indexOf('@') - 1) > 0 &&
                                (!email.endsWith("."))) {
                            if (password.isEmpty()) {
                                // No password found
                                tvSignUpError.setText(R.string.errorNoPassword);
                            } else if (!password.equals(confirm_password)) {
                                // Passwords do not match
                                tvSignUpError.setText(R.string.errorNotMatchingPasswords);
                            } else {
                                // Sign up user only if username/email and password are valid
                                signUpUser(username, email, password);
                            }
                        } else {
                            // Invalid email detected (incorrect syntax)
                            // Valid example: "example@example.example"
                            tvSignUpError.setText(R.string.errorInvalidEmail);
                        }
                    } else {
                        // No email found
                        tvSignUpError.setText(R.string.errorNoEmail);
                    }
                } else {
                    // No username found
                    tvSignUpError.setText(R.string.errorNoUsername);
                }
            }
        });

        // Listener for existing account
        btnExistingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick existing account button");
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    // Helper method that creates Parse user and launches search activity
    private void signUpUser(String username, String email, String password) {
        Log.i(TAG, "Attempting to sign up user with username " + username);
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with sign up", e);
                    Toast.makeText(SignUpActivity.this, "Issue with sign up!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SignUpActivity.this.goSearchActivity();
                Toast.makeText(SignUpActivity.this, "Sign up success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goSearchActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
