package com.lite.pits_jawwal.pitstracklite;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import java.util.Locale;

public class Config {
    private static Config mInstance;

    public Context thisContext;

    public Config(Context thisContext) {
        this.thisContext = thisContext;
    }

    public static Config getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new Config(context);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return mInstance;
    }


    public static String getAppLanguageStr(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = prefs.getString(Preferences.language1, "");
        return lang .equals("1") ? "en" : "ar";
    }
    public void setAppLocale() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(thisContext);
        String lang = prefs.getString(Preferences.language1, "");
        if(lang.equals("1")){//eng
            setEnglishLocale();
        }else{//ar
            setArabicLocale();
        }
    }
    public void setArabicLocale() {
        Locale arabicLocale = new Locale("ar");
        Locale.setDefault(arabicLocale);
        Configuration anConfiguration = new Configuration();
        anConfiguration.locale = arabicLocale;
        thisContext.getResources().updateConfiguration(anConfiguration, thisContext.getResources().getDisplayMetrics());
    }

    public void setEnglishLocale() {
        Locale arabicLocale = new Locale("en");
        Locale.setDefault(arabicLocale);
        Configuration anConfiguration = new Configuration();
        anConfiguration.locale = arabicLocale;
        thisContext.getResources().updateConfiguration(anConfiguration, thisContext.getResources().getDisplayMetrics());
    }
}
