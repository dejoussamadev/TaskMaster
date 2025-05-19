package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.adapter.TaskAdapter;
import com.example.taskmaster.dao.TaskDao;
import com.example.taskmaster.model.Task;
import com.example.taskmaster.util.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TaskListActivity extends BaseActivity {
    private RecyclerView recyclerViewTasks;
    private TextView textViewWelcome, textViewNoTasks;
    private FloatingActionButton fabAddTask;
    private SessionManager sessionManager;
    private TaskDao taskDao;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_task_list);

        // Initialize views
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewNoTasks = findViewById(R.id.textViewNoTasks);
        fabAddTask = findViewById(R.id.fabAddTask);

        // Initialize SessionManager and TaskDAO
        sessionManager = new SessionManager(this);
        taskDao = new TaskDao(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            // Navigate to LoginActivity
            Intent intent = new Intent(TaskListActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Set welcome message
        String welcomeText = getString(R.string.welcome, sessionManager.getUsername());
        textViewWelcome.setText(welcomeText);

        // Setup RecyclerView
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        loadTasks();

        // Show or hide FAB based on user type
        if (sessionManager.isManager()) {
            fabAddTask.setVisibility(View.VISIBLE);
            fabAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to AddTaskActivity to create a new task
                    Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            fabAddTask.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload tasks when returning to this activity
        loadTasks();
    }

    private void loadTasks() {
        if (sessionManager.isManager()) {
            // Managers can see all tasks
            taskList = taskDao.getAllTasks();
        } else {
            // Team members can see only tasks assigned to them
            taskList = taskDao.getTasksByAssignee(sessionManager.getUserId());
        }

        if (taskList.isEmpty()) {
            textViewNoTasks.setVisibility(View.VISIBLE);
            recyclerViewTasks.setVisibility(View.GONE);
        } else {
            textViewNoTasks.setVisibility(View.GONE);
            recyclerViewTasks.setVisibility(View.VISIBLE);

            taskAdapter = new TaskAdapter(this, taskList);
            taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Task task) {
                    // Navigate to TaskDetailActivity to view/edit task
                    Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
                    intent.putExtra("task_id", task.getId());
                    startActivity(intent);
                }
            });
            recyclerViewTasks.setAdapter(taskAdapter);
        }
    }
}