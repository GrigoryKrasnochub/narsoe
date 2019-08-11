package win.grishanya.narsoe;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class CallSoundControl {

    private static Integer phoneRingStatus = null;
    private static Context context = null;
    private static AudioManager audioManager = null;

    public CallSoundControl(Context contextTemp){
        context = contextTemp;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void turnOffSound (){
        phoneRingStatus = audioManager.getRingerMode();
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        Log.d("CallSound","Mute");
    }

    public void returnSoundStatus (){
        audioManager.setRingerMode(phoneRingStatus);
        Log.d("CallSound","UnMute");
    }
}
