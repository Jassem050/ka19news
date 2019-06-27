package com.amitzinfy.ka19news.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // shared preference mode
    private static final int PRIVATE_MODE = 0;
    // shared preference name
    private static final String PREF_NAME =  "com.amitzinfy.ka19news_pref";

    private static final String IS_FIRST_TIME_LAUNCH = "isFirstTimeLaunch";

    @SuppressLint("CommitPrefEdits")
    public PreferenceManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editor.apply();
    }

    public boolean isFirstTimeLauncg(){
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
