package com.example.taskmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.R;
import com.example.taskmaster.dao.UserDao;
import com.example.taskmaster.model.Task;
import com.example.taskmaster.model.User;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private List<Task> taskList;
    private OnItemClickListener listener;
    private UserDao userDao;

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        this.userDao = new UserDao(context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.textViewTaskTitle.setText(task.getTitle());
        holder.textViewTaskDescription.setText(task.getDescription());
        holder.textViewTaskStatus.setText("Status: " + task.getStatus());

        // Get assignee username
        User assignee = userDao.getUserById(task.getAssignedTo());
        String assigneeName = assignee != null ? assignee.getUsername() : "User #" + task.getAssignedTo();
        holder.textViewAssignedTo.setText("Assigned to: " + assigneeName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(task);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTaskTitle, textViewTaskDescription, textViewTaskStatus, textViewAssignedTo;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskTitle = itemView.findViewById(R.id.textViewTaskTitle);
            textViewTaskDescription = itemView.findViewById(R.id.textViewTaskDescription);
            textViewTaskStatus = itemView.findViewById(R.id.textViewTaskStatus);
            textViewAssignedTo = itemView.findViewById(R.id.textViewAssignedTo);
        }
    }
}