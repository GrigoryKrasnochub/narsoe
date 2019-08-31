package win.grishanya.narsoe.language;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.util.Arrays;
import java.util.Locale;

public class LanguageController {
    private String [] availableLanguages = {"en", "ru"};
    private Resources resources;
    private Context context;
    private Activity activity = null;
    private Class activityClass = null;

    public LanguageController (Resources res, Context context, AppCompatActivity activity, Class activityClass){
        resources = res;
        this.context = context;
        this.activity = activity;
        this.activityClass = activityClass;
        setLanguage();
    }

    public LanguageController (Resources res, Context context){
        resources = res;
        this.context = context;
        setLanguage();
    }

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.locale = myLocale;
        resources.updateConfiguration(conf, dm);

        if (activity != null) {
            Intent refresh = new Intent(context, activityClass);
            activity.finish();

            context.startActivity(refresh);
        }
    }

    public void setLanguage() {
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(context);
        String savedLanguage = myPreferences.getString("appLanguage",null);
        SharedPreferences.Editor myEditor = myPreferences.edit();

        String installedLanguage = resources.getConfiguration().locale.getLanguage();
        String defLanguage = Locale.getDefault().getLanguage();

        if (savedLanguage != null && !savedLanguage.equals(installedLanguage) ){
            setLocale(savedLanguage);
        }else{

            if(savedLanguage == null){
                String languageToSave = defLanguage;
                if(!Arrays.asList(availableLanguages).contains(defLanguage)){
                     languageToSave = "en";
                };
                myEditor.putString("appLanguage", languageToSave);
                myEditor.apply();
            }

            if(!installedLanguage.equals(defLanguage) && !savedLanguage.equals(installedLanguage)){
                if(!Arrays.asList(availableLanguages).contains(defLanguage)){
                    defLanguage = "en";
                };
                myEditor.putString("appLanguage", defLanguage);
                myEditor.apply();
                setLocale(defLanguage);
            }
        }
    }
}
