package win.grishanya.narsoe;

import android.app.Application;
import android.content.Context;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class CallBlock {

    public interface  DisconnectIncomingCallCallBack{
        void onNumberBlocked ();
        void onFailedNumberBlock ();
    }

    public void disconnectIncomingCall(Context context, DisconnectIncomingCallCallBack disconnectIncomingCallCallBack ) {
        ITelephony telephonyService;
        try {

                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    Method m = tm.getClass().getDeclaredMethod("getITelephony");

                    m.setAccessible(true);
                    telephonyService = (ITelephony) m.invoke(tm);

                    telephonyService.endCall();
                    disconnectIncomingCallCallBack.onNumberBlocked();

                } catch (Exception e) {
                    e.printStackTrace();
                    disconnectIncomingCallCallBack.onFailedNumberBlock();
                }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
