package com.example.taskmaster.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "TaskMasterPrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(int userId, String username, String userType) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_TYPE, userType);
        editor.commit();
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, 0);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getUserType() {
        return pref.getString(KEY_USER_TYPE, null);
    }

    public boolean isManager() {
        return "MANAGER".equals(getUserType());
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}
