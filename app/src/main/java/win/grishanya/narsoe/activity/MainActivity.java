package win.grishanya.narsoe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.tinkoff.decoro.FormattedTextChangeListener;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;
import win.grishanya.narsoe.R;
import win.grishanya.narsoe.language.LanguageController;
import win.grishanya.narsoe.permissions.PermissionChecer;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;
    private PermissionChecer permissionChecer;
    private Button searchButton;
    private EditText phoneNumberEditText;
    private BottomNavigationView navigation;

    //Navigation view


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                        Intent intent = new Intent(MainActivity.this, RecentCallsActivity.class);
                        MainActivity.this.finish();
                        startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("appLanguage")) {
            MainActivity.this.recreate();
        }
    }

    //ActivityMethods
    @Override
    protected void onPause() {
        super.onPause();
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LanguageController languageController = new LanguageController(this.getResources(), this,MainActivity.this,MainActivity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageController languageController = new LanguageController(this.getResources(), this,MainActivity.this,MainActivity.class);
        setContentView(R.layout.activity_main);
        sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        MaskImpl mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER);
        mask.setShowingEmptySlots(true);
        final FormatWatcher formatWatcher = new MaskFormatWatcher(mask);
        formatWatcher.installOn(phoneNumberEditText);

        formatWatcher.setCallback(new FormattedTextChangeListener() {
            @Override
            public boolean beforeFormatting(String oldValue, String newValue) {
                return false;
            }

            @Override
            public void onTextFormatted(FormatWatcher formatter, String newFormattedText) {
                String unformatedPhoneNumber = formatWatcher.getMask().toUnformattedString().replace("_","");

                //ToDO вынести в отдельный метод
                if(unformatedPhoneNumber.length()>11) {
                    searchButton.setEnabled(true);
                }else{
                    searchButton.setEnabled(false);
                }
            }
        });

        //Search Button Handler
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NumberInfoActivity.class);
                intent.putExtra("phoneNumber",formatWatcher.getMask().toUnformattedString().replace("_",""));
                startActivity(intent);
            }
        });

        permissionChecer = new PermissionChecer(MainActivity.this,this);
        permissionChecer.checkPermissions();

    }

    public String getIsValidPhonNumber(){
        //ToDO дописать :3
        boolean result = false;

        String userPhoneNumber = phoneNumberEditText.getText().toString();

        return userPhoneNumber;
    }


    //necessary to check READ_CALL_LOG READ_PHONE_STATE
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionChecer.checkPermissionsOnResult(grantResults);
    }

    //necessary to check ACTION_MANAGE_OVERLAY_PERMISSION
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionChecer.checkPermissionsResultByRequestCode(requestCode);
    }
}

