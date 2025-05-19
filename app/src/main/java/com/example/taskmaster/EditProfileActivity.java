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

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextFirstname, editTextLastname, editTextEmail, editTextPassword;
    private Button buttonSave;
    private TextView textViewMessage;
    private UserDao userDao;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editTextFirstname = findViewById(R.id.editTextFirstname);
        editTextLastname = findViewById(R.id.editTextLastname);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSave = findViewById(R.id.buttonSave);
        textViewMessage = findViewById(R.id.textViewMessage);

        userDao = new UserDao(this);

        // Assume userId is passed via Intent
        int userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            finish();
            return;
        }

        currentUser = userDao.getUserById(userId);
        if (currentUser == null) {
            finish();
            return;
        }

        // Populate fields
        editTextFirstname.setText(currentUser.getFirstname());
        editTextLastname.setText(currentUser.getLastname());
        editTextEmail.setText(currentUser.getEmail());
        editTextPassword.setText(currentUser.getPassword());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = editTextFirstname.getText().toString().trim();
                String lastname = editTextLastname.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    textViewMessage.setText("All fields are required.");
                    textViewMessage.setVisibility(View.VISIBLE);
                    return;
                }

                // Optionally check if email is used by another user
                User userWithEmail = userDao.getUserByEmail(email);
                if (userWithEmail != null && userWithEmail.getId() != currentUser.getId()) {
                    textViewMessage.setText("Email already in use.");
                    textViewMessage.setVisibility(View.VISIBLE);
                    return;
                }

                currentUser.setFirstname(firstname);
                currentUser.setLastname(lastname);
                currentUser.setEmail(email);
                currentUser.setPassword(password);

                int rows = userDao.updateUser(currentUser);
                if (rows > 0) {
                    textViewMessage.setText("Profile updated successfully.");
                } else {
                    textViewMessage.setText("Update failed.");
                }
                textViewMessage.setVisibility(View.VISIBLE);
            }
        });
    }
}