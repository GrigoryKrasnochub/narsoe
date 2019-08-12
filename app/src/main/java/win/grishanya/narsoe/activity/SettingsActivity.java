package win.grishanya.narsoe.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

import win.grishanya.narsoe.R;

public class SettingsActivity extends AppCompatActivity {

    private Switch defineIncomingCalls;
    private Switch closeModalWindowWhenCallApply;
    private Switch blockSpamCalls;
    private SeekBar modalWindowPosition;
    private Button ratingBorderPrefix;
    private EditText inputRatingBottomBorder;
    private SharedPreferences myPreferences;
    private ViewGroup windowLayout = null;
    private WindowManager windowManager = null;
    private Boolean modalWidowCreated = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        defineIncomingCalls = (Switch) findViewById(R.id.switchDefineIncomingCalls);
        closeModalWindowWhenCallApply = (Switch) findViewById(R.id.settingsCloseModalWindowWhenCallApplySwitch);
        blockSpamCalls = (Switch) findViewById(R.id.settingsActivityBlockSpamCallsSwitch);
        modalWindowPosition = (SeekBar) findViewById(R.id.seekBarModalWindowVerticalPosition);
        inputRatingBottomBorder = (EditText) findViewById(R.id.settingsActivityInputRatingBottomBorder);
        ratingBorderPrefix = (Button) findViewById(R.id.settingActivityChangeRatingPrefixButton);




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
                if (!s.toString().equals("")) {
                    String fullRatingBottomBorder = myPreferences.getInt("fullRatingBottomBorder",-20) >= 0 ? "+" : "-";
                    myEditor.putInt("ratingBottomBorder", Integer.parseInt(s.toString()));
                    myEditor.putInt("fullRatingBottomBorder",Integer.parseInt(fullRatingBottomBorder + s.toString()));
                    myEditor.apply();
                }
            }
        });

        ratingBorderPrefix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = ratingBorderPrefix.getText().toString();
                int buttonTextData = Integer.parseInt(buttonText + "1");
                switch (buttonText){
                    case "+":
                        buttonText = "-";
                        break;

                    case "-":
                        buttonText = "+";
                        break;
                }
                ratingBorderPrefix.setText(buttonText);
                myEditor.putString("ratingBorderPrefix", buttonText);
                myEditor.putInt("fullRatingBottomBorder",myPreferences.getInt("fullRatingBottomBorder",-20) * buttonTextData);
                myEditor.apply();
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
        inputRatingBottomBorder.setText(String.valueOf(myPreferences.getInt("ratingBottomBorder",20)));
        ratingBorderPrefix.setText(myPreferences.getString("ratingBorderPrefix","-"));
        modalWindowPosition.setProgress((myPreferences.getInt("modalWindowPosition",15)*100)/getUserScreenHeight());
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
