package com.example.taskmaster.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmaster.database.DatabaseHelper;
import com.example.taskmaster.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public User authenticate(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String[] columns = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USERNAME,
                DatabaseHelper.COLUMN_PASSWORD,
                DatabaseHelper.COLUMN_USER_TYPE,
                DatabaseHelper.COLUMN_USER_FIRSTNAME,
                DatabaseHelper.COLUMN_USER_LASTNAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_CREATED_AT
        };

        String selection = DatabaseHelper.COLUMN_USERNAME + " = ? AND " +
                DatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            user = extractUserFromCursor(cursor);
        }

        cursor.close();
        db.close();
        return user;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String[] columns = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USERNAME,
                DatabaseHelper.COLUMN_USER_TYPE,
                DatabaseHelper.COLUMN_USER_FIRSTNAME,
                DatabaseHelper.COLUMN_USER_LASTNAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_CREATED_AT
        };

        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            user = extractUserFromCursor(cursor);
        }

        cursor.close();
        db.close();
        return user;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String[] columns = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USERNAME,
                DatabaseHelper.COLUMN_USER_TYPE,
                DatabaseHelper.COLUMN_USER_FIRSTNAME,
                DatabaseHelper.COLUMN_USER_LASTNAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_CREATED_AT
        };

        String selection = DatabaseHelper.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            user = extractUserFromCursor(cursor);
        }

        cursor.close();
        db.close();
        return user;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String[] columns = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USERNAME,
                DatabaseHelper.COLUMN_USER_TYPE,
                DatabaseHelper.COLUMN_USER_FIRSTNAME,
                DatabaseHelper.COLUMN_USER_LASTNAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_CREATED_AT
        };

        String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            user = extractUserFromCursor(cursor);
        }

        cursor.close();
        db.close();
        return user;
    }

    public long insertUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, user.getUsername());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COLUMN_USER_TYPE, user.getUserType());
        values.put(DatabaseHelper.COLUMN_USER_FIRSTNAME, user.getFirstname());
        values.put(DatabaseHelper.COLUMN_USER_LASTNAME, user.getLastname());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_USER_CREATED_AT, System.currentTimeMillis());

        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
        return result;
    }

    private User extractUserFromCursor(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME)));
        user.setUserType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_TYPE)));
        user.setFirstname(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FIRSTNAME)));
        user.setLastname(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_LASTNAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)));
        user.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_CREATED_AT)));
        return user;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_FIRSTNAME, user.getFirstname());
        values.put(DatabaseHelper.COLUMN_USER_LASTNAME, user.getLastname());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        int rows = db.update(DatabaseHelper.TABLE_USERS, values,
                DatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
        return rows;
    }

    public List<User> getAllTeamMembers() {
        List<User> teamMembers = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USER_FIRSTNAME,
                DatabaseHelper.COLUMN_USER_LASTNAME,
                DatabaseHelper.COLUMN_USERNAME,
                DatabaseHelper.COLUMN_USER_TYPE,
                DatabaseHelper.COLUMN_USER_EMAIL
        };
        String selection = DatabaseHelper.COLUMN_USER_TYPE + "=?";
        String[] selectionArgs = { "TEAM_MEMBER" };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null, null, null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
                user.setFirstname(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FIRSTNAME)));
                user.setLastname(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_LASTNAME)));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME)));
                user.setUserType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_TYPE)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)));
                teamMembers.add(user);
            }
            cursor.close();
        }
        return teamMembers;
    }
}