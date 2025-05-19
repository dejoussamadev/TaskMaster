package com.example.taskmaster;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.taskmaster.dao.TaskDao;
import com.example.taskmaster.model.Task;
import com.example.taskmaster.util.SessionManager;
import com.example.taskmaster.model.User;
import com.example.taskmaster.dao.UserDao;

import java.util.ArrayList;
import java.util.List;

public class AddTaskActivity extends BaseActivity {
    private EditText editTextTitle, editTextDescription;
    private Spinner spinnerStatus, spinnerAssignee;
    private Button buttonSave;
    private TaskDao taskDao;
    private UserDao userDao;
    private SessionManager sessionManager;
    private List<User> teamMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_add_task);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerAssignee = findViewById(R.id.spinnerAssignee);
        buttonSave = findViewById(R.id.buttonSave);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this, R.array.task_status_options, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        taskDao = new TaskDao(this);
        userDao = new UserDao(this);
        sessionManager = new SessionManager(this);

        // Load team members for assignee spinner
        teamMembers = userDao.getAllTeamMembers();
        List<String> memberNames = new ArrayList<>();
        for (User user : teamMembers) {
            memberNames.add(user.getFirstname() + " " + user.getLastname());
        }
        ArrayAdapter<String> assigneeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, memberNames);
        assigneeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssignee.setAdapter(assigneeAdapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String status = spinnerStatus.getSelectedItem().toString();
                int assigneePosition = spinnerAssignee.getSelectedItemPosition();

                if (title.isEmpty()) {
                    editTextTitle.setError("Title required");
                    return;
                }
                if (assigneePosition == Spinner.INVALID_POSITION) {
                    Toast.makeText(AddTaskActivity.this, "Select an assignee", Toast.LENGTH_SHORT).show();
                    return;
                }

                int assigneeId = teamMembers.get(assigneePosition).getId();

                Task task = new Task();
                task.setTitle(title);
                task.setDescription(description);
                task.setStatus(status);
                task.setAssigneeId(assigneeId);
                task.setCreatedBy(sessionManager.getUserId());

                taskDao.insertTask(task);
                Toast.makeText(AddTaskActivity.this, "Task created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}