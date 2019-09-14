package win.grishanya.narsoe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import win.grishanya.narsoe.R;
import win.grishanya.narsoe.ResponseDataHandler;
import win.grishanya.narsoe.dataClasses.ExpandedRecyclerViewData;
import win.grishanya.narsoe.network.PhoneNumberHandler;
import win.grishanya.narsoe.widgets.ExpandedRecyclerView.ExpandedRecyclerView;

public class NumberInfoActivity extends AppCompatActivity {

    TextView phoneNumberTextView;
    TextView informationTextView;
    TextView generalInfoTitleTextView;
    TextView generalInfoTextTextView;
    RecyclerView generalInfoTextRecyclerView;
    ImageView generalInfoTitleImageImageView;
    LinearLayout generalInfoLinearLayout;
    LinearLayout generalInfoContentWrapper;
    ProgressBar downloadProgressBar;
    private SharedPreferences myPreferences;
    String phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_info);

        PhoneNumberHandler phoneNumberHandler = new PhoneNumberHandler();

        phoneNumberTextView = (TextView) findViewById(R.id.numberInfoPhoneNumberTextView);
        informationTextView = (TextView) findViewById(R.id.numberInfoFullINformationTextView);
        generalInfoTitleTextView = (TextView) findViewById(R.id.numberInfoActivityGeneralInfoTitleTextView);
        generalInfoTextTextView = (TextView) findViewById(R.id.numberInfoActivityGeneralInfoTextTextView);
        generalInfoTitleImageImageView = (ImageView) findViewById(R.id.numberInfoActivityGeneralTitleImageImageView);
        generalInfoLinearLayout = (LinearLayout) findViewById(R.id.numberInfoActivityGeneralInfoWrapper);
        generalInfoTextRecyclerView = (RecyclerView) findViewById(R.id.numberInfoActivityGeneralInfoTextRecyclerView);
        generalInfoContentWrapper = (LinearLayout) findViewById(R.id.numberInfoActivityGeneralInfoContentWrapper);
        downloadProgressBar = (ProgressBar) findViewById(R.id.numberInfoProgressBar);

        informationTextView.setMovementMethod(new ScrollingMovementMethod());

        myPreferences =  PreferenceManager.getDefaultSharedPreferences(this);

        //ExpandedTextView generalInformationView = new ExpandedTextView(generalInfoLinearLayout,generalInfoTitleTextView,generalInfoTextTextView,generalInfoTitleImageImageView,getApplicationContext());
        ArrayList<ExpandedRecyclerViewData> expandedRecyclerViewData = new ArrayList<ExpandedRecyclerViewData>();
        ExpandedRecyclerViewData ex = new ExpandedRecyclerViewData();
        ex.Header = "OOO";
        ex.Info = "Moya Obobrona";
        expandedRecyclerViewData.add(ex);
        expandedRecyclerViewData.add(ex);
        ExpandedRecyclerView generalInformationView = new ExpandedRecyclerView(generalInfoLinearLayout,generalInfoContentWrapper,generalInfoTitleTextView,generalInfoTitleImageImageView,generalInfoTextRecyclerView,expandedRecyclerViewData,this);

        Intent intent = getIntent();
        phoneNumber = phoneNumberHandler.prettifyPhoneNumber(intent.getStringExtra("phoneNumber"));
        phoneNumberTextView.setText(phoneNumber);
        ShowNumberInfo(phoneNumber);

    }



    protected void ShowNumberInfo(final String phoneNumber){
        ResponseDataHandler responseDataHandler = new ResponseDataHandler(myPreferences.getString("domainURL","https://narsoe.ga/"));
        responseDataHandler.getFullNumberInfo(phoneNumber, getResources(),new ResponseDataHandler.NumberInfoCallbacks() {
            @Override
            public void onGetNumberInfo(String result) {
                informationTextView.setText(result);
                downloadProgressBar.setVisibility(View.INVISIBLE);
                informationTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGetNumberInfoFailed(Throwable error) {
                informationTextView.setText(R.string.bad_request);
                informationTextView.setVisibility(View.VISIBLE);
                ShowNumberInfo(phoneNumber);
            }
        });
    }
}

