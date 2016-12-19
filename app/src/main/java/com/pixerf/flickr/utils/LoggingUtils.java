package com.pixerf.flickr.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.pixerf.flickr.R;
import com.pixerf.flickr.model.AuthResponse;

/**
 * created by Aamer on 12/19/2016.
 */

public class LoggingUtils {

    private static final String TAG = LoggingUtils.class.getSimpleName();

    private Context context;

    public LoggingUtils(Context context) {
        this.context = context;
    }

    public void saveResponseToPreferences(AuthResponse authResponse) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.login_info_preferences), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(context.getString(R.string.full_token), authResponse.getToken());
            editor.putString(context.getString(R.string.perms), authResponse.getPermission());
            editor.putString(context.getString(R.string.user_id), authResponse.getUser().getUserId());
            editor.putString(context.getString(R.string.user_name), authResponse.getUser().getUsername());
            editor.putString(context.getString(R.string.full_name), authResponse.getUser().getFullName());
            editor.putBoolean(context.getString(R.string.logged_in), true);
            editor.apply();
            Log.e(TAG, "Login info saved to preferences");
        } finally {
            context = null;
        }
    }

    public boolean isLoggedIn() {
        try {
            SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.login_info_preferences), Context.MODE_PRIVATE);
            return preferences.getBoolean(context.getString(R.string.logged_in), false);
        } finally {
            context = null;
        }
    }

    public void clearPreferences() {
        try {
            SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.login_info_preferences), Context.MODE_PRIVATE);
            preferences.edit().clear().apply();
        } finally {
            context = null;
        }
    }

    // TODO: 12/19/2016 Remove this..
    public void displayPreferencesInLog() {
        //display user info
        try {
            SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.login_info_preferences), Context.MODE_PRIVATE);
            Log.e(TAG, preferences.getString(context.getResources().getString(R.string.user_name), null));
            Log.e(TAG, preferences.getString(context.getResources().getString(R.string.full_token), null));
        } finally {
            context = null;
        }
    }
}
