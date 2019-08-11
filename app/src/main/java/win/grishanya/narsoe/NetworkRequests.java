package win.grishanya.narsoe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import win.grishanya.narsoe.dataClasses.InfoListShort;
import win.grishanya.narsoe.dataClasses.InfoRating;
import win.grishanya.narsoe.network.GetShortInformation;
import win.grishanya.narsoe.network.PhoneNumberHandler;
import win.grishanya.narsoe.network.RetrofitInstance;

public class NetworkRequests {

    public interface MakeRequestCallbacks {
        void onGetResponse (Response<InfoListShort> response);
        void onGetFailed (Throwable error);
    }

    public interface MakeRatingRequestCallbacks {
        void onGetResponse (Response<InfoRating> response);
        void onGetFailed (Throwable error);
    }



    public static void MakeRequest(String number, final MakeRequestCallbacks makeRequestCallbacks){
        PhoneNumberHandler phoneNumberHandler = new PhoneNumberHandler();
        number = phoneNumberHandler.validatePhoneNumber(number);

        GetShortInformation service = RetrofitInstance.getRetrofitInstance().create(GetShortInformation.class);
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

    public static void MakeRatingRequest(String number, final MakeRatingRequestCallbacks makeRatingRequestCallbacks){
        PhoneNumberHandler phoneNumberHandler = new PhoneNumberHandler();
        number = phoneNumberHandler.validatePhoneNumber(number);

        GetShortInformation service = RetrofitInstance.getRetrofitInstance().create(GetShortInformation.class);
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
