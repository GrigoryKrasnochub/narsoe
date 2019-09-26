package win.grishanya.narsoe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import win.grishanya.narsoe.NumberInfoWrapperRecyclerView;
import win.grishanya.narsoe.R;
import win.grishanya.narsoe.ResponseDataHandler;
import win.grishanya.narsoe.dataClasses.ExpandedRecyclerViewData;
import win.grishanya.narsoe.dataClasses.ExpandedViewsWrapper;
import win.grishanya.narsoe.network.PhoneNumberHandler;

public class NumberInfoActivity extends AppCompatActivity {

    TextView phoneNumberTextView;
    TextView informationTextView;
    RecyclerView infoTextWrapperRecyclerView;
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
        infoTextWrapperRecyclerView = (RecyclerView) findViewById(R.id.numberInfoActivityInformationWrapperRecyclerView);
        downloadProgressBar = (ProgressBar) findViewById(R.id.numberInfoProgressBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        infoTextWrapperRecyclerView.setLayoutManager(layoutManager);

        myPreferences =  PreferenceManager.getDefaultSharedPreferences(this);


        Intent intent = getIntent();
        phoneNumber = phoneNumberHandler.prettifyPhoneNumber(intent.getStringExtra("phoneNumber"));
        phoneNumberTextView.setText(phoneNumber);
        ShowNumberInfo(phoneNumber);

    }



    protected void ShowNumberInfo(final String phoneNumber){
        ResponseDataHandler responseDataHandler = new ResponseDataHandler(myPreferences.getString("domainURL","https://narsoe.ga/"));
        responseDataHandler.getFullNumberInfoInExpandedViewsWrapper(phoneNumber, getResources(), new ResponseDataHandler.NumberInfoFullInExpandedViewsWrapperCallbacks() {
            @Override
            public void onGetNumberInfo(ArrayList<ExpandedViewsWrapper> result) {
                NumberInfoWrapperRecyclerView numberInfoWrapperRecyclerView = new NumberInfoWrapperRecyclerView(result);
                infoTextWrapperRecyclerView.setAdapter(numberInfoWrapperRecyclerView);

                downloadProgressBar.setVisibility(View.GONE);
                informationTextView.setVisibility(View.GONE);
            }
            @Override
            public void onGetNumberInfoFailed(Throwable error) {
                informationTextView.setText(R.string.bad_request);
                informationTextView.setVisibility(View.VISIBLE);
                ShowNumberInfo(phoneNumber);
            }
        });
//                new ResponseDataHandler.NumberInfoCallbacks() {
//            @Override
//            public void onGetNumberInfo(String result) {
//                informationTextView.setText(result);
//                downloadProgressBar.setVisibility(View.INVISIBLE);
//                informationTextView.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onGetNumberInfoFailed(Throwable error) {
//                informationTextView.setText(R.string.bad_request);
//                informationTextView.setVisibility(View.VISIBLE);
//                ShowNumberInfo(phoneNumber);
//            }
//        });
    }
}

