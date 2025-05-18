package com.example.taskmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "taskmaster.db";
    private static final int DATABASE_VERSION = 1;

    // User table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_USER_TYPE = "user_type"; // "MANAGER" or "TEAM_MEMBER"

    // Task table
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_TITLE = "title";
    public static final String COLUMN_TASK_DESCRIPTION = "description";
    public static final String COLUMN_TASK_STATUS = "status"; // "NEW", "IN_PROGRESS", "COMPLETED"
    public static final String COLUMN_TASK_ASSIGNED_TO = "assigned_to"; // user_id of assignee
    public static final String COLUMN_TASK_CREATED_BY = "created_by"; // user_id of creator
    public static final String COLUMN_TASK_CREATED_AT = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_USER_TYPE + " TEXT"
                + ")";

        // Create tasks table
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TASK_TITLE + " TEXT,"
                + COLUMN_TASK_DESCRIPTION + " TEXT,"
                + COLUMN_TASK_STATUS + " TEXT,"
                + COLUMN_TASK_ASSIGNED_TO + " INTEGER,"
                + COLUMN_TASK_CREATED_BY + " INTEGER,"
                + COLUMN_TASK_CREATED_AT + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_TASK_ASSIGNED_TO + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
                + "FOREIGN KEY(" + COLUMN_TASK_CREATED_BY + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
                + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);

        // Insert default users for testing
        insertDefaultUsers(db);
    }

    private void insertDefaultUsers(SQLiteDatabase db) {
        ContentValues managerValues = new ContentValues();
        managerValues.put(COLUMN_USERNAME, "manager");
        managerValues.put(COLUMN_PASSWORD, "password123");
        managerValues.put(COLUMN_USER_TYPE, "MANAGER");
        db.insert(TABLE_USERS, null, managerValues);

        ContentValues teamMemberValues = new ContentValues();
        teamMemberValues.put(COLUMN_USERNAME, "team_member");
        teamMemberValues.put(COLUMN_PASSWORD, "password123");
        teamMemberValues.put(COLUMN_USER_TYPE, "TEAM_MEMBER");
        db.insert(TABLE_USERS, null, teamMemberValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}