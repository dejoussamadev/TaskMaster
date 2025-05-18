package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.taskmaster.dao.UserDao;
import com.example.taskmaster.model.User;
import com.example.taskmaster.util.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewError;
    private UserDao userDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewError = findViewById(R.id.textViewError);

        // Initialize DAO and SessionManager
        userDao = new UserDao(this);
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            // Navigate to the TaskListActivity
            Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
            startActivity(intent);
            finish();
        }

        // Set up login button click listener
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Basic validation
                if (username.isEmpty() || password.isEmpty()) {
                    textViewError.setText("Please enter both username and password");
                    textViewError.setVisibility(View.VISIBLE);
                    return;
                }

                // Authenticate user
                User user = userDao.authenticate(username, password);
                if (user != null) {
                    // Create login session
                    sessionManager.createLoginSession(user.getId(), user.getUsername(), user.getUserType());

                    // Navigate to the TaskListActivity
                    Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    textViewError.setText(R.string.invalid_credentials);
                    textViewError.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}