package com.example.taskmaster.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmaster.database.DatabaseHelper;
import com.example.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    private final DatabaseHelper dbHelper;

    public TaskDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long createTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TASK_TITLE, task.getTitle());
        values.put(DatabaseHelper.COLUMN_TASK_DESCRIPTION, task.getDescription());
        values.put(DatabaseHelper.COLUMN_TASK_STATUS, task.getStatus());
        values.put(DatabaseHelper.COLUMN_TASK_ASSIGNED_TO, task.getAssignedTo());
        values.put(DatabaseHelper.COLUMN_TASK_CREATED_BY, task.getCreatedBy());
        values.put(DatabaseHelper.COLUMN_TASK_CREATED_AT, System.currentTimeMillis());

        long id = db.insert(DatabaseHelper.TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    public boolean updateTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TASK_TITLE, task.getTitle());
        values.put(DatabaseHelper.COLUMN_TASK_DESCRIPTION, task.getDescription());
        values.put(DatabaseHelper.COLUMN_TASK_STATUS, task.getStatus());
        values.put(DatabaseHelper.COLUMN_TASK_ASSIGNED_TO, task.getAssignedTo());

        int rowsAffected = db.update(
                DatabaseHelper.TABLE_TASKS,
                values,
                DatabaseHelper.COLUMN_TASK_ID + " = ?",
                new String[]{String.valueOf(task.getId())}
        );

        db.close();
        return rowsAffected > 0;
    }

    public Task getTaskById(int taskId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Task task = null;

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_TASKS,
                null,
                DatabaseHelper.COLUMN_TASK_ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_ID);
            int titleIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_TITLE);
            int descIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_DESCRIPTION);
            int statusIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_STATUS);
            int assignedToIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_ASSIGNED_TO);
            int createdByIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_CREATED_BY);
            int createdAtIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_CREATED_AT);

            if (idIdx >= 0 && titleIdx >= 0 && descIdx >= 0 && statusIdx >= 0 &&
                    assignedToIdx >= 0 && createdByIdx >= 0 && createdAtIdx >= 0) {
                task = new Task();
                task.setId(cursor.getInt(idIdx));
                task.setTitle(cursor.getString(titleIdx));
                task.setDescription(cursor.getString(descIdx));
                task.setStatus(cursor.getString(statusIdx));
                task.setAssignedTo(cursor.getInt(assignedToIdx));
                task.setCreatedBy(cursor.getInt(createdByIdx));
                task.setCreatedAt(cursor.getLong(createdAtIdx));
            }
        }

        cursor.close();
        db.close();
        return task;
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_TASKS,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COLUMN_TASK_CREATED_AT + " DESC"
        );

        if (cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_ID);
            int titleIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_TITLE);
            int descIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_DESCRIPTION);
            int statusIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_STATUS);
            int assignedToIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_ASSIGNED_TO);
            int createdByIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_CREATED_BY);
            int createdAtIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_CREATED_AT);

            if (idIdx >= 0 && titleIdx >= 0 && descIdx >= 0 && statusIdx >= 0 &&
                    assignedToIdx >= 0 && createdByIdx >= 0 && createdAtIdx >= 0) {
                do {
                    Task task = new Task();
                    task.setId(cursor.getInt(idIdx));
                    task.setTitle(cursor.getString(titleIdx));
                    task.setDescription(cursor.getString(descIdx));
                    task.setStatus(cursor.getString(statusIdx));
                    task.setAssignedTo(cursor.getInt(assignedToIdx));
                    task.setCreatedBy(cursor.getInt(createdByIdx));
                    task.setCreatedAt(cursor.getLong(createdAtIdx));
                    taskList.add(task);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return taskList;
    }

    public List<Task> getTasksByAssignee(int userId) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_TASKS,
                null,
                DatabaseHelper.COLUMN_TASK_ASSIGNED_TO + " = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                DatabaseHelper.COLUMN_TASK_CREATED_AT + " DESC"
        );

        if (cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_ID);
            int titleIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_TITLE);
            int descIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_DESCRIPTION);
            int statusIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_STATUS);
            int assignedToIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_ASSIGNED_TO);
            int createdByIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_CREATED_BY);
            int createdAtIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_CREATED_AT);

            if (idIdx >= 0 && titleIdx >= 0 && descIdx >= 0 && statusIdx >= 0 &&
                    assignedToIdx >= 0 && createdByIdx >= 0 && createdAtIdx >= 0) {
                do {
                    Task task = new Task();
                    task.setId(cursor.getInt(idIdx));
                    task.setTitle(cursor.getString(titleIdx));
                    task.setDescription(cursor.getString(descIdx));
                    task.setStatus(cursor.getString(statusIdx));
                    task.setAssignedTo(cursor.getInt(assignedToIdx));
                    task.setCreatedBy(cursor.getInt(createdByIdx));
                    task.setCreatedAt(cursor.getLong(createdAtIdx));
                    taskList.add(task);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return taskList;
    }

    // Add this method to TaskDao.java

    public long insertTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TASK_TITLE, task.getTitle());
        values.put(DatabaseHelper.COLUMN_TASK_DESCRIPTION, task.getDescription());
        values.put(DatabaseHelper.COLUMN_TASK_STATUS, task.getStatus());
        values.put(DatabaseHelper.COLUMN_TASK_ASSIGNED_TO, task.getAssignedTo());
        values.put(DatabaseHelper.COLUMN_TASK_CREATED_BY, task.getCreatedBy());
        values.put(DatabaseHelper.COLUMN_TASK_CREATED_AT, System.currentTimeMillis());
        long id = db.insert(DatabaseHelper.TABLE_TASKS, null, values);
        db.close();
        return id;
    }
}