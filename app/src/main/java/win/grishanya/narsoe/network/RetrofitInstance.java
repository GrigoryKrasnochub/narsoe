package win.grishanya.narsoe.network;

import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://narsoe.ga/";


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            Interceptor timeoutInterceptor = new Interceptor() {

                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();

                    int connectTimeout = chain.connectTimeoutMillis();
                    int readTimeout = chain.readTimeoutMillis();
                    int writeTimeout = chain.writeTimeoutMillis();

                    String connectNew = request.header("CONNECT_TIMEOUT");
                    String readNew = request.header("READ_TIMEOUT");
                    String writeNew = request.header("WRITE_TIMEOUT");

                    if (!TextUtils.isEmpty(connectNew)) {
                        connectTimeout = Integer.valueOf(connectNew);
                    }
                    if (!TextUtils.isEmpty(readNew)) {
                        readTimeout = Integer.valueOf(readNew);
                    }
                    if (!TextUtils.isEmpty(writeNew)) {
                        writeTimeout = Integer.valueOf(writeNew);
                    }

                    Request.Builder builder = request.newBuilder();
                    builder.removeHeader("CONNECT_TIMEOUT");
                    builder.removeHeader("READ_TIMEOUT");
                    builder.removeHeader("WRITE_TIMEOUT");

                    return chain
                            .withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                            .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
                            .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                            .proceed(builder.build());
                }
            };

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(timeoutInterceptor)
                    .build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
