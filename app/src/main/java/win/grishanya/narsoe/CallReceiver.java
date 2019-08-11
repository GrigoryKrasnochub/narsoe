package win.grishanya.narsoe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Response;
import win.grishanya.narsoe.dataClasses.InfoListShort;
import win.grishanya.narsoe.dataClasses.InfoRating;
import win.grishanya.narsoe.network.PhoneNumberHandler;

public class CallReceiver extends BroadcastReceiver {
    private static Boolean incomingCall = false;
    private static WindowManager windowManager;
    private static SharedPreferences myPreferences = null;
    private static ViewGroup windowLayout;

    @Override
    public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();

            if (myPreferences == null){
                myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            }

            if (action !=null && myPreferences.getBoolean("defineIncomingCalls",true)) {
                if (action.equals("android.intent.action.PHONE_STATE")) {
                    String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                    if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        //Трубка не поднята, телефон звонит

                        final CallSoundControl callSoundControl = new CallSoundControl(context);
                        callSoundControl.turnOffSound();

                        incomingCall = true;

                        String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                        PhoneNumberHandler phoneNumberHandler = new PhoneNumberHandler();
                        final String peatyPhoneNumber = phoneNumberHandler.prettifyPhoneNumber(phoneNumber);

                        if(myPreferences.getBoolean("blockSpamCalls",false)) {
                            NetworkRequests.MakeRatingRequest(phoneNumber, new NetworkRequests.MakeRatingRequestCallbacks(){
                                @Override
                                public void onGetResponse(Response<InfoRating> response) {
                                    if (response.body().getRating() > myPreferences.getInt("ratingBottomBorder",-20)) {
                                        callSoundControl.returnSoundStatus();
                                        showWindow(context, peatyPhoneNumber);
                                    } else {
                                        CallBlock callBlock = new CallBlock();
                                        callBlock.disconnectIncomingCall(context, new CallBlock.DisconnectIncomingCallCallBack() {
                                            @Override
                                            public void onNumberBlocked() {
                                                callSoundControl.returnSoundStatus();
                                            }

                                            @Override
                                            public void onFailedNumberBlock() {
                                                callSoundControl.returnSoundStatus();
                                                showWindow(context, peatyPhoneNumber);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onGetFailed(Throwable error) {
                                    callSoundControl.returnSoundStatus();
                                    showWindow(context, peatyPhoneNumber);
                                }
                            });

                        }else{
                            callSoundControl.returnSoundStatus();
                            showWindow(context, peatyPhoneNumber);
                        }

                    } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                        //Телефон находится в режиме звонка (набор номера при исходящем звонке / разговор)
                        if (myPreferences.getBoolean("closeModalWindowWhenCallApply",true)) {
                            if (incomingCall) {
                                incomingCall = false;
                                closeWindow();
                            }
                        }
                    } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        //Телефон находится в ждущем режиме - это событие наступает по окончанию разговора
                        //или в ситуации "отказался поднимать трубку и сбросил звонок".
                        if (incomingCall) {
                            incomingCall = false;
                            closeWindow();
                        }
                    }
                }
            }
        }

    public void showWindow(final Context context, String phone) {
        int modalWindowPosition = myPreferences.getInt("modalWindowPosition", 15);


        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //ToDO добавить TYPE_SYSTEM_ALERT для api 23
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        //Если добавить  | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY | WindowManager.LayoutParams. TYPE_APPLICATION_OVERLAY отображаетс на весь экран

        //Верстка
        params.gravity = Gravity.TOP;
        params.y = modalWindowPosition;
        windowLayout = (ViewGroup) layoutInflater.inflate(R.layout.call_info, null);
        windowLayout.setBackgroundResource(R.color.colorBackGroundInfo);

        TextView textViewNumber=(TextView) windowLayout.findViewById(R.id.textViewNumber);
        Button buttonClose=(Button) windowLayout.findViewById(R.id.buttonClose);
        textViewNumber.setText(phone);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWindow();
            }
        });

        final TextView informationTextView = (TextView) windowLayout.findViewById(R.id.informationTextView);
        ResponseDataHandler responseDataHandler = new ResponseDataHandler();
        ResponseDataHandler.NumberInfoCallbacks numberInfoCallbacks = new ResponseDataHandler.NumberInfoCallbacks() {
            @Override
            public void onGetNumberInfo(String result) {
                informationTextView.setText(result);
            }

            @Override
            public void onGetNumberInfoFailed(Throwable error) {
                informationTextView.setText(R.string.bad_request);
            }
        };
        responseDataHandler.getNumberInfo(phone,numberInfoCallbacks);
        windowManager.addView(windowLayout, params);
    }

    public void closeWindow() {
        if (windowLayout != null){
            windowManager.removeView(windowLayout);
            windowLayout = null;
        }
    }

}