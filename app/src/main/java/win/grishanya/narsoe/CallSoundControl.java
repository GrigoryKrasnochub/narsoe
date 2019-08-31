package win.grishanya.narsoe;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class CallSoundControl {


    private static Integer phoneRingStatus = null;
    private static AudioManager audioManager = null;

    public interface CallSoundControlCallBack{
         void onSoundMuted();
    }

    public CallSoundControl(Context context){
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void turnOffSound (){
        phoneRingStatus = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        audioManager.setStreamVolume(AudioManager.STREAM_RING,AudioManager.ADJUST_MUTE,0);
        Log.d("CallSound","Mute");
    }

    public void returnSoundStatus (){
        audioManager.setStreamVolume(AudioManager.STREAM_RING,phoneRingStatus,0);
        Log.d("CallSound",phoneRingStatus.toString());
        Log.d("CallSound","UnMute");
    }
}
