package com.example.myapplication;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Hardcoded login credentials
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "123456";

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        // Set click listener for login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation checks
        if (username.isEmpty()) {
            etUsername.setError("Please enter a username!");
            etUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Please enter a password!");
            etPassword.requestFocus();
            return;
        }

        // Hardcoded login check
        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
        }
    }
}
