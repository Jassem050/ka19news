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
    private static final String LANGUAGE_NAME = "languagename";
    private static final String IS_FEED_FIRST_TIME_LAUNCH = "isFeedFirstTimeLaunch";

    // user info
    private static final String USER_ID = "userid";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String USER_STATUS = "user_status";
    private static final String APP_STATUS = "app_status";
    private static final String USER_NAME = "username";
    private static final String USER_EMAIL = "email";
    private static final String USER_PHONE_NO = "phoneno";
    private static final String USER_GENDER = "gender";
    private static final String USER_DOB = "dob";
    private static final String USER_ADDRESS = "address";
    private static final String NEWS_POSTED = "newsPosted";
    private static final String NEWS_ACCEPTED = "newsAccepted";

    // add news constants
    private static final String LANGUAGE_ID = "languageId";
    private static final String LANGUAGE_NAME_NEWS = "languageName";
    private static final String CATEGORY_ID_NEWS = "categoryId";
    private static final String NEWS_TITLE = "newsTitle";
    private static final String NEWS_IMAGE_URL = "newsImageUrl";
    private static final String NEWS_CONTENT = "newsContent";
    private static final String IMG_CHOOSER = "imgChooser";


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

    public boolean isFirstTimeLaunch(){
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setCategory(String categoryIds){
        editor.putString(CATEGORY_ID, categoryIds);
        editor.apply();
    }

    public String getCategory(){
        return pref.getString(CATEGORY_ID, "id");
    }

    public void clearCategory(){
        editor.remove(CATEGORY_ID);
        editor.apply();
    }

    public void setIds(String id){
        editor.putString(ID, id);
        editor.apply();
    }

    public String getIds(){
        return pref.getString(ID, "id");
    }

    // set language id for news

    public void setLanguageName(String languageName){
        editor.putString(LANGUAGE_NAME, languageName);
        editor.apply();
    }

    public String getLanguageName() {
        return pref.getString(LANGUAGE_NAME, "English");
    }

    public void setIsFeedFirstTimeLaunch(int number){
        editor.putInt(IS_FEED_FIRST_TIME_LAUNCH, number);
        editor.apply();
    }

    public int getIsFeedFirstTimeLaunch(){
        return pref.getInt(IS_FEED_FIRST_TIME_LAUNCH, 1);
    }

    /*
    * user part
    * */
    public void setUserId(String userId){
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        return pref.getString(USER_ID, "userid");
    }

    public void setAccessToken(String accessToken){
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, "access_token");
    }

    public void setUserStatus(String userStatus){
        editor.putString(USER_STATUS, userStatus);
        editor.apply();
    }

    public String getUserStatus(){
        return pref.getString(USER_STATUS, "logged_out");
    }

    public void setAppStatus(String appStatus){
        editor.putString(APP_STATUS, appStatus);
        editor.apply();
    }

    public String getAppStatus(){
        return pref.getString(APP_STATUS, "reader");
    }

    public void setUserName(String userName){
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public String getUserName(){
        return pref.getString(USER_NAME, null);
    }

    public void setUserEmail(String userEmail){
        editor.putString(USER_EMAIL, userEmail);
        editor.apply();
    }

    public String getUserEmail(){
        return pref.getString(USER_EMAIL, null);
    }

    public void setUserPhoneNo(String userPhoneNo){
        editor.putString(USER_PHONE_NO, userPhoneNo);
        editor.apply();
    }

    public String getUserPhoneNo(){
        return pref.getString(USER_PHONE_NO, null);
    }

    public void setUserAddress(String userAddress){
        editor.putString(USER_ADDRESS, userAddress);
        editor.apply();
    }

    public String getUserAddress(){
        return pref.getString(USER_ADDRESS, null);
    }

    public void setUserGender(String userGender){
        editor.putString(USER_GENDER, userGender);
        editor.apply();
    }

    public String getUserGender(){
        return pref.getString(USER_GENDER, null);
    }

    public void setUserDob(String userDob){
        editor.putString(USER_DOB, userDob);
        editor.apply();
    }

    public String getUserDob(){
        return pref.getString(USER_DOB, null);
    }

    public void setNewsPosted(String newsPosted){
        editor.putString(NEWS_POSTED, newsPosted);
        editor.apply();
    }

    public String getNewsPosted(){
        return pref.getString(NEWS_POSTED, null);
    }

    public void setNewsAccepted(String newsAccepted){
        editor.putString(NEWS_ACCEPTED, newsAccepted);
        editor.apply();
    }

    public String getNewsAccepted(){
        return pref.getString(NEWS_ACCEPTED, null);
    }

    /*
    *   Add News methods
    * */

    public void setLanguageId(String languageId){
        editor.putString(LANGUAGE_ID, languageId);
        editor.apply();
    }

    public String getLanguageId(){
        return pref.getString(LANGUAGE_ID, null);
    }

    public void setLanguageNameNews(String languageNameNews){
        editor.putString(LANGUAGE_NAME_NEWS, languageNameNews);
        editor.apply();
    }

    public String getLanguageNameNews(){
        return pref.getString(LANGUAGE_NAME_NEWS, null);
    }

    // news category
    public void setCategoryIdNews(String categoryIdNews){
        editor.putString(CATEGORY_ID_NEWS, categoryIdNews);
        editor.apply();
    }

    public String getCategoryIdNews(){
        return pref.getString(CATEGORY_ID_NEWS, null);
    }

    // news title
    public void setNewsTitle(String newsTitle){
        editor.putString(NEWS_TITLE, newsTitle);
        editor.apply();
    }

    public String getNewsTitle(){
        return pref.getString(NEWS_TITLE, null);
    }

    // news image
    public void setNewsImageUrl(String newsImageUrl){
        editor.putString(NEWS_IMAGE_URL, newsImageUrl);
        editor.apply();
    }

    public String getNewsImageUrl(){
        return pref.getString(NEWS_IMAGE_URL, null);
    }

    // news content
    public void setNewsContent(String newsContent){
        editor.putString(NEWS_CONTENT, newsContent);
        editor.apply();
    }

    public String getNewsContent(){
        return pref.getString(NEWS_CONTENT, null);
    }

    public void setImgChooser(String imgChooser){
        editor.putString(IMG_CHOOSER, imgChooser);
        editor.apply();
    }

    public String getImgChooser(){
        return pref.getString(IMG_CHOOSER, null);
    }
}
