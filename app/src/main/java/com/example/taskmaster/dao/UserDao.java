package com.example.taskmaster.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmaster.database.DatabaseHelper;
import com.example.taskmaster.model.User;

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
                DatabaseHelper.COLUMN_USER_TYPE
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
            int idIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID);
            int usernameIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME);
            int passwordIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD);
            int userTypeIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_TYPE);

            if (idIdx >= 0 && usernameIdx >= 0 && passwordIdx >= 0 && userTypeIdx >= 0) {
                user = new User();
                user.setId(cursor.getInt(idIdx));
                user.setUsername(cursor.getString(usernameIdx));
                user.setPassword(cursor.getString(passwordIdx));
                user.setUserType(cursor.getString(userTypeIdx));
            }
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
                DatabaseHelper.COLUMN_USER_TYPE
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
            int idIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID);
            int usernameIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME);
            int userTypeIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_TYPE);

            if (idIdx >= 0 && usernameIdx >= 0 && userTypeIdx >= 0) {
                user = new User();
                user.setId(cursor.getInt(idIdx));
                user.setUsername(cursor.getString(usernameIdx));
                user.setUserType(cursor.getString(userTypeIdx));
            }
        }

        cursor.close();
        db.close();
        return user;
    }
}