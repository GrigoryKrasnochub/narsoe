package win.grishanya.narsoe.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import win.grishanya.narsoe.Calls;
import win.grishanya.narsoe.R;
import win.grishanya.narsoe.RecentCallsRecycleViewAdapter;
import win.grishanya.narsoe.network.PhoneNumberHandler;

public class RecentCallsActivity extends AppCompatActivity {
    private BottomNavigationView navigation;
    private EditText stringQueryEditText;
    private ArrayList<Calls> recentCallsList;
    private RecentCallsRecycleViewAdapter recentCallsRecycleViewAdapter;
    private String [] listOfRecentCallsTables = new String []{
            CallLog.Calls._ID,
            CallLog.Calls.DATE,
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.DURATION,
            CallLog.Calls.TYPE
    };
    private RecyclerView recentCalls;

    //Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_recent_calls);
                    return true;
                case R.id.navigation_home:
                    Intent intent = new Intent(RecentCallsActivity.this, MainActivity.class);
                    startActivity(intent);
                    //mTextMessage.setText(R.string.title_search);
                    return true;
            }
            return false;
        }
    };

    //Activity Methods
    @Override
    protected void onPause() {
        super.onPause();
        navigation.setSelectedItemId(R.id.navigation_dashboard);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recentCallsRecycleViewAdapter.updateListOfRecentCalls(getListOfRecentCalls(),!stringQueryEditText.getText().toString().equals(""));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_calls);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        recentCalls = (RecyclerView) findViewById(R.id.recentCallsRecyclerView);
        stringQueryEditText = (EditText) findViewById(R.id.recentCallsSearchQuery);

        navigation.setSelectedItemId(R.id.navigation_dashboard);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recentCalls.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recentCalls.getContext(),
                 layoutManager.getOrientation());
        recentCalls.addItemDecoration(mDividerItemDecoration);

        //ToDO Получается что сазу полсе этого запускается onResume, который повторно вносит данные. Надо фиксить
        this.recentCallsList = getListOfRecentCalls();
        recentCallsRecycleViewAdapter = new RecentCallsRecycleViewAdapter(recentCallsList, new RecentCallsRecycleViewAdapter.ItemClickListener() {
            @Override
            public void onItemLongClick(String number) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", number);
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.recentCallsActivityNumberCopied), Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onItemClick(String number) {
                Intent intent = new Intent(RecentCallsActivity.this, NumberInfoActivity.class);
                intent.putExtra("phoneNumber",number);
                startActivity(intent);
            }
        });
        recentCalls.setAdapter(recentCallsRecycleViewAdapter);

        stringQueryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        stringQueryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("TextChanged","TextChanged");
                recentCallsRecycleViewAdapter.showSearchInTheList(stringQueryEditText.getText().toString());
            }
        });
    }

    //Метод отдает список всех звонков
    public ArrayList<Calls> getListOfRecentCalls(){
        PhoneNumberHandler phoneNumberHandler = new PhoneNumberHandler();

        ArrayList<Calls> result = new ArrayList<Calls>();
        Cursor listOfRecentCalls = getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                listOfRecentCallsTables,
                null,
                null,
                CallLog.Calls.DATE + " DESC"
        );
        try {
            if (listOfRecentCalls.moveToFirst()) {
                do {
                    Calls calls = new Calls();
                    calls._id = listOfRecentCalls.getInt(listOfRecentCalls.getColumnIndex(CallLog.Calls._ID));
                    calls.number = phoneNumberHandler.prettifyPhoneNumber(listOfRecentCalls.getString(listOfRecentCalls.getColumnIndex(CallLog.Calls.NUMBER)));
                    long timeStamp = listOfRecentCalls.getLong(listOfRecentCalls.getColumnIndex(CallLog.Calls.DATE));
                    calls.date = new Date(timeStamp);
                    String name = listOfRecentCalls.getString(listOfRecentCalls.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    calls.name = name == null ? "Unknown" : listOfRecentCalls.getString(listOfRecentCalls.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    calls.duration = listOfRecentCalls.getString(listOfRecentCalls.getColumnIndex(CallLog.Calls.DURATION));
                    calls.type = listOfRecentCalls.getInt(listOfRecentCalls.getColumnIndex(CallLog.Calls.TYPE));
                    result.add(calls);
                } while (listOfRecentCalls.moveToNext());
            }
            if (!listOfRecentCalls.isClosed()) {
                listOfRecentCalls.close();
            }
        }catch (java.lang.NullPointerException e){
            Log.d("exception","NullPointerExceptionDB");
        }
        return result;
    }

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
                Intent intent = new Intent(RecentCallsActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


