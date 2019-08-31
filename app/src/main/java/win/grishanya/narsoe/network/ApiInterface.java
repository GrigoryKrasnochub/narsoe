package win.grishanya.narsoe.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import win.grishanya.narsoe.dataClasses.InfoListShort;
import win.grishanya.narsoe.dataClasses.InfoRating;
import win.grishanya.narsoe.dataClasses.ServerVersion;

public interface ApiInterface {
    @Headers({"CONNECT_TIMEOUT:5000", "READ_TIMEOUT:5000", "WRITE_TIMEOUT:5000"})
    @GET("api/search/all/")
    Call<InfoListShort> getData(@Query("phone") String phoneNumber);

    @Headers({"CONNECT_TIMEOUT:1000", "READ_TIMEOUT:1000", "WRITE_TIMEOUT:1000"})
    @GET("api/search/rating/")
    Call<InfoRating> getRating(@Query("phone") String phoneNumber);

    @Headers({"CONNECT_TIMEOUT:5000", "READ_TIMEOUT:5000", "WRITE_TIMEOUT:5000"})
    @GET("api/version/")
    Call<ServerVersion> getVersion();
}
