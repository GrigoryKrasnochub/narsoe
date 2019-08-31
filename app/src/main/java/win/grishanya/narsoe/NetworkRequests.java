package win.grishanya.narsoe;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.Preference;
import android.preference.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import win.grishanya.narsoe.activity.SettingsActivity;
import win.grishanya.narsoe.dataClasses.InfoListShort;
import win.grishanya.narsoe.dataClasses.InfoRating;
import win.grishanya.narsoe.dataClasses.ServerVersion;
import win.grishanya.narsoe.network.ApiInterface;
import win.grishanya.narsoe.network.PhoneNumberHandler;
import win.grishanya.narsoe.network.RetrofitInstance;

public class NetworkRequests {
    private String URL;

    public NetworkRequests(String url){
        URL = url;
    }

    public interface MakeRequestCallbacks {
        void onGetResponse (Response<InfoListShort> response);
        void onGetFailed (Throwable error);
    }

    public interface MakeRatingRequestCallbacks {
        void onGetResponse (Response<InfoRating> response);
        void onGetFailed (Throwable error);
    }

    public interface MakeServerVersionRequestCallbacks {
        void onGetResponse (Response<ServerVersion> response);
        void onGetFailed (Throwable error);
    }

    public void MakeServerVersionRequest(final MakeServerVersionRequestCallbacks makeRequestCallbacks){
        ApiInterface service = RetrofitInstance.getRetrofitInstance(URL).create(ApiInterface.class);
        Call<ServerVersion> call = service.getVersion();
        call.enqueue(new Callback<ServerVersion>() {
            @Override
            public void onResponse(Call<ServerVersion> call, Response<ServerVersion> response) {
                if (response.code()==200){
                    makeRequestCallbacks.onGetResponse(response);
                }else{
                    Throwable t = new Throwable();
                    makeRequestCallbacks.onGetFailed(t);
                }
            }

            @Override
            public void onFailure(Call<ServerVersion> call, Throwable t) {
                makeRequestCallbacks.onGetFailed(t);
            }
        });
    }

    public void MakeRequest(String number, final MakeRequestCallbacks makeRequestCallbacks){
        PhoneNumberHandler phoneNumberHandler = new PhoneNumberHandler();
        number = phoneNumberHandler.validatePhoneNumber(number);

        ApiInterface service = RetrofitInstance.getRetrofitInstance(URL).create(ApiInterface.class);
        Call<InfoListShort> call = service.getData(number);
        call.enqueue(new Callback<InfoListShort>() {
            @Override
            public void onResponse(Call<InfoListShort> call, Response<InfoListShort> response) {
                if (response.code()==200){
                    makeRequestCallbacks.onGetResponse(response);
                }else{
                    Throwable t = new Throwable();
                    makeRequestCallbacks.onGetFailed(t);
                }
            }

            @Override
            public void onFailure(Call<InfoListShort> call, Throwable t) {
                makeRequestCallbacks.onGetFailed(t);
            }
        });
    }

    public void MakeRatingRequest(String number, final MakeRatingRequestCallbacks makeRatingRequestCallbacks){
        PhoneNumberHandler phoneNumberHandler = new PhoneNumberHandler();
        number = phoneNumberHandler.validatePhoneNumber(number);

        ApiInterface service = RetrofitInstance.getRetrofitInstance(URL).create(ApiInterface.class);
        Call<InfoRating> call = service.getRating(number);
        call.enqueue(new Callback<InfoRating>() {
            @Override
            public void onResponse(Call<InfoRating> call, Response<InfoRating> response) {
                if (response.code()==200){
                    makeRatingRequestCallbacks.onGetResponse(response);
                }else{
                    Throwable t = new Throwable();
                    makeRatingRequestCallbacks.onGetFailed(t);
                }
            }

            @Override
            public void onFailure(Call<InfoRating> call, Throwable t) {
                makeRatingRequestCallbacks.onGetFailed(t);
            }
        });
    }

}
