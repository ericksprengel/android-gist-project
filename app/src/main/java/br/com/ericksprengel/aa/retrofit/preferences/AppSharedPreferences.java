package br.com.ericksprengel.aa.retrofit.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class AppSharedPreferences {

    private static final String KEY_USER_LOGIN = "user_login";

    public static String getUserLogin(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(KEY_USER_LOGIN, "ericksprengel");
    }

    public static void setUserLogin(Activity activity, String userLogin) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_USER_LOGIN, userLogin);
        editor.apply();
    }
}
