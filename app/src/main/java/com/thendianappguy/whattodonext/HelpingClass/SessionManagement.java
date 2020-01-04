package com.thendianappguy.whattodonext.HelpingClass;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

public class SessionManagement {

    //Shared Preferences
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Context _context;
    int PRIVATE_MODE = 0;

    // Shared preference file name
    private static final String PREF_NAME = "AppPref";
    // All Shared Pref Keys
    private static final String KEY_NAME = "name";
    private static final String KEY_UID = "uid";
    private static final String IS_LOGIN = "isloggedin";
    private static final String KEY_EMAIL = "email";

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        preferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    /** Create login session */

    public void createLoginSession(String name, String email, String uid){

        // Storing login value as true
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_UID,uid);

        editor.commit();
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGIN,false);
    }

    public String getUserUid(){
        return preferences.getString(KEY_UID,"");
    }

    public String getUserName(){
        return preferences.getString(KEY_NAME,"");
    }

    public String getUserEmail(){
        return preferences.getString(KEY_EMAIL,"");
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();
        //Logout from Firebase
        FirebaseAuth.getInstance().signOut();
      /*  // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, SignIn.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);*/
    }

}
