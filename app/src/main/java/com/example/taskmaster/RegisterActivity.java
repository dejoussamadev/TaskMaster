// app/src/main/java/com/example/taskmaster/RegisterActivity.java
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

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextFirstname, editTextLastname, editTextEmail;
    private Button buttonRegister;
    private TextView textViewError;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFirstname = findViewById(R.id.editTextFirstname);
        editTextLastname = findViewById(R.id.editTextLastname);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewError = findViewById(R.id.textViewError);

        userDao = new UserDao(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String firstname = editTextFirstname.getText().toString().trim();
                String lastname = editTextLastname.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || email.isEmpty()) {
                    textViewError.setText("Please fill in all fields");
                    textViewError.setVisibility(View.VISIBLE);
                    return;
                }

                if (userDao.getUserByUsername(username) != null) {
                    textViewError.setText("Username already exists");
                    textViewError.setVisibility(View.VISIBLE);
                    return;
                }

                if (userDao.getUserByEmail(email) != null) {
                    textViewError.setText("Email already registered");
                    textViewError.setVisibility(View.VISIBLE);
                    return;
                }

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setEmail(email);
                user.setUserType("TEAM_MEMBER"); // Default type

                long result = userDao.insertUser(user);
                if (result > 0) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    textViewError.setText("Registration failed. Try again.");
                    textViewError.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}