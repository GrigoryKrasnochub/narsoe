package win.grishanya.narsoe;

import android.app.Application;
import android.content.Context;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class CallBlock {
    public void disconnectIncomingCall(Context context) {
        ITelephony telephonyService;
        try {

                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    Method m = tm.getClass().getDeclaredMethod("getITelephony");

                    m.setAccessible(true);
                    telephonyService = (ITelephony) m.invoke(tm);

                    telephonyService.endCall();

                } catch (Exception e) {
                    e.printStackTrace();
                }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
