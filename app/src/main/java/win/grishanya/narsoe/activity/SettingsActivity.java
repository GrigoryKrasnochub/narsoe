package win.grishanya.narsoe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import win.grishanya.narsoe.R;
import win.grishanya.narsoe.ResponseDataHandler;
import win.grishanya.narsoe.language.LanguageController;

public class SettingsActivity extends AppCompatActivity {

    private Switch defineIncomingCalls;
    private Switch closeModalWindowWhenCallApply;
    private Switch blockSpamCalls;
    private Spinner choseLanguage;
    private SeekBar modalWindowPosition;
    private EditText inputRatingBottomBorder;
    private EditText domainInfo;
    private TextView serverVersion;
    private SharedPreferences myPreferences;
    private ViewGroup windowLayout = null;
    private WindowManager windowManager = null;
    private Boolean modalWidowCreated = false;
    private String[] Languages = {"en","ru"};
    private Boolean languageSpinnerInitialized = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LanguageController languageController = new LanguageController(this.getResources(), this,SettingsActivity.this,SettingsActivity.class);

        setContentView(R.layout.activity_settings);

        defineIncomingCalls = (Switch) findViewById(R.id.switchDefineIncomingCalls);
        closeModalWindowWhenCallApply = (Switch) findViewById(R.id.settingsCloseModalWindowWhenCallApplySwitch);
        blockSpamCalls = (Switch) findViewById(R.id.settingsActivityBlockSpamCallsSwitch);
        modalWindowPosition = (SeekBar) findViewById(R.id.seekBarModalWindowVerticalPosition);
        inputRatingBottomBorder = (EditText) findViewById(R.id.settingsActivityInputRatingBottomBorder);
        choseLanguage = (Spinner) findViewById(R.id.settingsActivityLanguageChoseSpinner);
        domainInfo = (EditText) findViewById(R.id.settingsActivityDomainURL);
        serverVersion = (TextView) findViewById(R.id.settingsActivityServerVersionTextView);

        myPreferences
                = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        final SharedPreferences.Editor myEditor = myPreferences.edit();

            showSaveSettings();


        defineIncomingCalls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myEditor.putBoolean("defineIncomingCalls",isChecked);
                myEditor.apply();
            }
        });

        closeModalWindowWhenCallApply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myEditor.putBoolean("closeModalWindowWhenCallApply",isChecked);
                myEditor.apply();
            }
        });

        blockSpamCalls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myEditor.putBoolean("blockSpamCalls",isChecked);
                myEditor.apply();
            }
        });

        inputRatingBottomBorder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches("[+\\-]?\\d+")) {
                    myEditor.putInt("ratingBottomBorder", Integer.parseInt(s.toString()));
                    myEditor.apply();
                }
            }
        });

        domainInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")) {
                    myEditor.putString("domainURL", s.toString());
                    myEditor.apply();
                    showServerVersion();
                }
            }
        });


        modalWindowPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int modalWindowPos = getUserScreenHeight()/100*progress;
                myEditor.putInt("modalWindowPosition",modalWindowPos);
                showModalWindowPosition(modalWindowPos);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myEditor.apply();
                if(modalWidowCreated) {
                    windowManager.removeView(windowLayout);
                    modalWidowCreated = false;
                }
            }
        });

        choseLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if (languageSpinnerInitialized){
                    String item = (String)parent.getItemAtPosition(position);
                    myEditor.putString("appLanguage",item);
                    myEditor.apply();
                    languageController.setLanguage();
               }
               languageSpinnerInitialized = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private String [] SpinnerSaveState (String[] languages){
      String savedLanguage = myPreferences.getString("appLanguage",null);
      if (savedLanguage != null){
          List<String> langArray = new LinkedList<>(Arrays.asList(languages));
          langArray.remove(savedLanguage);
          langArray.add(0,savedLanguage);
          langArray.toArray(languages);
      }
      return languages;
    }

    private int getUserScreenHeight (){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.y;
    }

    private void showSaveSettings (){
        defineIncomingCalls.setChecked(myPreferences.getBoolean("defineIncomingCalls",true));
        closeModalWindowWhenCallApply.setChecked(myPreferences.getBoolean("closeModalWindowWhenCallApply",true));
        blockSpamCalls.setChecked(myPreferences.getBoolean("blockSpamCalls",false));
        inputRatingBottomBorder.setText(String.valueOf(myPreferences.getInt("ratingBottomBorder",getResources().getInteger(R.integer.defaultSettingBottomRatingBorder))));
        modalWindowPosition.setProgress((myPreferences.getInt("modalWindowPosition",15)*100)/getUserScreenHeight());
        domainInfo.setText(myPreferences.getString("domainURL","https://narsoe.ga/"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,SpinnerSaveState(Languages));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choseLanguage.setAdapter(adapter);
        showServerVersion();
    }

    private void showServerVersion(){
        ResponseDataHandler responseDataHandler = new ResponseDataHandler(myPreferences.getString("domainURL","https://narsoe.ga/"));
        ResponseDataHandler.ServerVersionsCallbacks  serverVersionsCallbacks = new ResponseDataHandler.ServerVersionsCallbacks() {
            @Override
            public void onGetVersion(String result) {
                serverVersion.setTextColor(getColor(R.color.colorTextInfo));
                serverVersion.setText(result);
            }

            @Override
            public void onGetVersionFailed(Throwable error) {
                serverVersion.setText(R.string.setting_activity_server_version_request_error);
                serverVersion.setTextColor(getColor(R.color.colorAccent));
            }
        };
        responseDataHandler.getServerVersion(serverVersionsCallbacks);
    }

    private void showModalWindowPosition(int position) {
        if (!modalWidowCreated) {
            windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT);
            //Если добавить  | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY | WindowManager.LayoutParams. TYPE_APPLICATION_OVERLAY отображаетс на весь экран
            //Верстка
            params.alpha = 0.6f;
            params.gravity = Gravity.TOP;
            params.y = position;
            windowLayout = (ViewGroup) layoutInflater.inflate(R.layout.call_info, null);
            windowLayout.setBackgroundResource(R.color.colorBackGroundInfo);

            windowManager.addView(windowLayout, params);
            modalWidowCreated = true;
        }else {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT);
            params.alpha = 0.6f;
            params.gravity = Gravity.TOP;
            params.y = position;
            windowManager.updateViewLayout(windowLayout, params);
        }
    }
}
