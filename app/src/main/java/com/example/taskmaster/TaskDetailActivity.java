package com.example.taskmaster;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmaster.dao.TaskDao;
import com.example.taskmaster.model.Task;
import com.example.taskmaster.util.SessionManager;

public class TaskDetailActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextAssignTo;
    private Spinner spinnerStatus;
    private Button buttonSave;
    private TaskDao taskDao;
    private SessionManager sessionManager;
    private int taskId = 0;
    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextAssignTo = findViewById(R.id.editTextAssignTo);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        buttonSave = findViewById(R.id.buttonSave);

        // Initialize DAO and SessionManager
        taskDao = new TaskDao(this);
        sessionManager = new SessionManager(this);

        // Setup status spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.task_status_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        // Check if editing an existing task
        if (getIntent().hasExtra("task_id")) {
            taskId = getIntent().getIntExtra("task_id", 0);
            loadTaskDetails(taskId);
        }

        // Handle field visibility based on user type
        if (!sessionManager.isManager()) {
            // Team members can only update task status
            editTextTitle.setEnabled(false);
            editTextDescription.setEnabled(false);
            editTextAssignTo.setVisibility(View.GONE);
            findViewById(R.id.textViewAssignToLabel).setVisibility(View.GONE);
        }

        // Setup save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void loadTaskDetails(int taskId) {
        currentTask = taskDao.getTaskById(taskId);
        if (currentTask != null) {
            editTextTitle.setText(currentTask.getTitle());
            editTextDescription.setText(currentTask.getDescription());
            editTextAssignTo.setText(String.valueOf(currentTask.getAssignedTo()));

            // Set spinner selection based on status
            String status = currentTask.getStatus();
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerStatus.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().equals(status)) {
                    spinnerStatus.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();
        String assignToStr = editTextAssignTo.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        int assignTo;
        try {
            assignTo = Integer.parseInt(assignToStr);
        } catch (NumberFormatException e) {
            assignTo = sessionManager.getUserId(); // Default to current user
        }

        if (currentTask == null) {
            // Create new task
            Task newTask = new Task();
            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setStatus(status);
            newTask.setAssignedTo(assignTo);
            newTask.setCreatedBy(sessionManager.getUserId());
            newTask.setCreatedAt(System.currentTimeMillis());

            long result = taskDao.createTask(newTask);
            if (result > 0) {
                Toast.makeText(this, "Task created successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to create task", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing task
            currentTask.setTitle(title);
            currentTask.setDescription(description);
            currentTask.setStatus(status);
            currentTask.setAssignedTo(assignTo);

            boolean result = taskDao.updateTask(currentTask);
            if (result) {
                Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}