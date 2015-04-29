package com.example.lazarus.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Scott on 4/27/2015.
 */
public class SessionManager {
    SharedPreferences user_pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "HummingnerdPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_UID = "uid";
    private static final String KEY_GID = "gid";

    public SessionManager(Context context){
        this._context = context;
        user_pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = user_pref.edit();
    }

    public void createLoginSession(String uid, String gid){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_UID, uid);
        editor.putString(KEY_GID, gid);

        editor.commit();
    }

    public HashMap<String,String> getUserDetail(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_GID, user_pref.getString(KEY_GID, null));

        user.put(KEY_UID, user_pref.getString(KEY_UID, null));

        return user;
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
        else{
            Log.v("Login", "here");
            Intent i = new Intent(_context, UserMenu.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public boolean isLoggedIn(){
        return user_pref.getBoolean(IS_LOGIN, false);
    }
}