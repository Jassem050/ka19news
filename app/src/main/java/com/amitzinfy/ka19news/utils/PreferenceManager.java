package com.amitzinfy.ka19news.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static PreferenceManager preferenceManager;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
//    private Context _context;

    // shared preference mode
    private static final int PRIVATE_MODE = 0;
    // shared preference name
    private static final String PREF_NAME =  "com.amitzinfy.ka19news_pref";

    private static final String IS_FIRST_TIME_LAUNCH = "isFirstTimeLaunch";
    private static final String CATEGORY_ID = "id";
    private static final String ID = "c_id";


    public static PreferenceManager getInstance(Context context){
        if (preferenceManager == null){
            preferenceManager = new PreferenceManager(context);
        }
        return preferenceManager;
    }

    @SuppressLint("CommitPrefEdits")
    public PreferenceManager(Context context){
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editor.apply();
    }

    public boolean isFirstTimeLauncg(){
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setCategory(String categoryIds){
        editor.putString(CATEGORY_ID, categoryIds);
        editor.apply();
    }

    public String getCategory(){
        return pref.getString(CATEGORY_ID, "id");
    }

    public void setIds(String id){
        editor.putString(ID, id);
        editor.apply();
    }

    public String getIds(){
        return pref.getString(ID, "id");
    }

}
