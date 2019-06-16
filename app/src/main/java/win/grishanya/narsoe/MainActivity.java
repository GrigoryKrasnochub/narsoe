package win.grishanya.narsoe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.tinkoff.decoro.FormattedTextChangeListener;
import ru.tinkoff.decoro.Mask;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText phoneNumberEditText;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_search);
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_recent_calls);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Search Button Handler
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(),getIsValidPhonNumber(),Toast.LENGTH_LONG);
                toast.show();
            }
        });

        //
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);

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
                    Toast toast = Toast.makeText(getApplicationContext(), unformatedPhoneNumber, Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    searchButton.setEnabled(false);
                }
            }
        });
    }

    public String getIsValidPhonNumber(){
        //ToDO дописать :3
        boolean result = false;

        String userPhoneNumber = phoneNumberEditText.getText().toString();

        return userPhoneNumber;
    }
}
