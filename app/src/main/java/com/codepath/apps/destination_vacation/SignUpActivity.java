package com.codepath.apps.destination_vacation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        btnSignUpScreen = findViewById(R.id.btnSignUpScreen);
        btnExistingAccount = findViewById(R.id.btnExistingAccount);


        // Listener for sign up button
        btnSignUpScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");
                String username = etUsernameSignUp.getText().toString();
                String email = etEmailSignUp.getText().toString();
                String password = etPasswordSignUp.getText().toString();
                String confirm_password = etConfirmPasswordSignUp.getText().toString();

                // Check for valid username, email and password
                // TODO Instead of toasts change the color of the EditText boxes
                if (!username.isEmpty()) {
                    if (email.contains("@") && email.contains(".")) {
                        if (password.isEmpty())
                            Toast.makeText(SignUpActivity.this, "Enter a password", Toast.LENGTH_SHORT).show();
                        else if (!password.equals(confirm_password))
                            Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        else
                            // Sign up user only if username, email, and password are valid
                            signUpUser(username, email, password);
                    }
                    else
                        Toast.makeText(SignUpActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(SignUpActivity.this, "Enter a username", Toast.LENGTH_SHORT).show();
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
