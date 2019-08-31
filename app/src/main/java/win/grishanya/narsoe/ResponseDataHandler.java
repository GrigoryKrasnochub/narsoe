package win.grishanya.narsoe;

import android.content.res.Resources;

import java.util.List;

import retrofit2.Response;
import win.grishanya.narsoe.dataClasses.InfoListShort;
import win.grishanya.narsoe.dataClasses.ServerVersion;

public class ResponseDataHandler {

    private String URL;

    public ResponseDataHandler (String url){
        URL = url;
    }

    public interface NumberInfoCallbacks{
        void onGetNumberInfo(String result);
        void onGetNumberInfoFailed(Throwable error);
    }

    public interface ServerVersionsCallbacks{
        void onGetVersion(String result);
        void onGetVersionFailed(Throwable error);
    }

    public void getServerVersion(final ServerVersionsCallbacks serverVersionsCallbacks) {
        NetworkRequests.MakeServerVersionRequestCallbacks makeServerVersionRequestCallbacks = new NetworkRequests.MakeServerVersionRequestCallbacks() {
            @Override
            public void onGetResponse(Response<ServerVersion> response) {
                if (response.body() != null) {
                    serverVersionsCallbacks.onGetVersion(response.body().getVersion());
                }else{
                    Throwable error = new Throwable();
                    serverVersionsCallbacks.onGetVersionFailed(error);
                }
            }

            @Override
            public void onGetFailed(Throwable error) {
                serverVersionsCallbacks.onGetVersionFailed(error);
            }
        };
        NetworkRequests networkRequests = new NetworkRequests(URL);
        networkRequests.MakeServerVersionRequest(makeServerVersionRequestCallbacks);
    }

    public void getNumberInfo(String number, final Resources resources, final NumberInfoCallbacks numberInfoCallbacks) {
        NetworkRequests.MakeRequestCallbacks makeRequestCallbacks = new NetworkRequests.MakeRequestCallbacks() {
            @Override
            public void onGetResponse(Response<InfoListShort> response) {
                String result = "";
                if(response.body().getCompany() != null) {
                    result = addStringIfNotEmpty(result,response.body().getCompany().getName(),resources.getString(R.string.responseHandler_company_name),true);
                    result = addStringIfNotEmpty(result,response.body().getCompany().getDescription(),resources.getString(R.string.responseHandler_company_description),true);
                }

                result = addStringIfNotEmpty(result,response.body().getName(),resources.getString(R.string.responseHandler_name),true);
                result = addStringIfNotEmpty(result,String.valueOf(response.body().getRating()),resources.getString(R.string.responseHandler_rating),true);
                if(response.body().getExtra() != null) {
                    if(response.body().getExtra().getAvito() != null) {
                        result = addStringIfNotEmpty(result, response.body().getExtra().getAvito().getName(), resources.getString(R.string.responseHandler_avito_name), true);
                    }
                }
                result = addStringIfNotEmpty(result,response.body().getType(),resources.getString(R.string.responseHandler_type),true);

                if(!response.body().getComments().isEmpty()){
                    result = addStringIfNotEmpty(result,response.body().getComments().get(0),resources.getString(R.string.responseHandler_comment),false);
                }
                numberInfoCallbacks.onGetNumberInfo(result);
            }

            @Override
            public void onGetFailed(Throwable error) {
                numberInfoCallbacks.onGetNumberInfoFailed(error);
            }
        };
        NetworkRequests networkRequests = new NetworkRequests(URL);
        networkRequests.MakeRequest(number, makeRequestCallbacks);
    }

    public void getFullNumberInfo (String number, final Resources resources, final NumberInfoCallbacks numberInfoCallbacks){
        NetworkRequests.MakeRequestCallbacks makeRequestCallbacks = new NetworkRequests.MakeRequestCallbacks() {
            @Override
            public void onGetResponse(Response<InfoListShort> response) {
                String result = "";
                if(response.body().getCompany() != null) {
                    result = addStringIfNotEmpty(result,response.body().getCompany().getName(),resources.getString(R.string.responseHandler_company_name),true);
                    result = addStringIfNotEmpty(result,response.body().getCompany().getDescription(),resources.getString(R.string.responseHandler_company_description),true);
                    result = addStringIfNotEmpty(result,response.body().getCompany().getCity(),resources.getString(R.string.responseHandler_company_city),true);
                    result = addStringIfNotEmpty(result,response.body().getCompany().getUrl(),resources.getString(R.string.responseHandler_company_url),true);
                    result = addStringIfNotEmpty(result,response.body().getCompany().getTelephone(),resources.getString(R.string.responseHandler_company_telephone),true);
                    result = addStringIfNotEmpty(result,response.body().getCompany().getAddress(),resources.getString(R.string.responseHandler_company_adress),true);
                    result = addStringIfNotEmpty(result,response.body().getCompany().getEmail(),resources.getString(R.string.responseHandler_company_email),true);
                    result += "\n";
                }

                result = addStringIfNotEmpty(result,response.body().getName(),resources.getString(R.string.responseHandler_name),true);
                result = addStringIfNotEmpty(result,String.valueOf(response.body().getRating()),resources.getString(R.string.responseHandler_rating),true);
                result = addStringIfNotEmpty(result,response.body().getType(),resources.getString(R.string.responseHandler_type),true);
                result = addStringIfNotEmpty(result,response.body().getRegion(),resources.getString(R.string.responseHandler_region),true);
                result += "\n";
                if(response.body().getExtra() != null) {
                    if(response.body().getExtra().getAvito() != null) {
                        result = addStringIfNotEmpty(result, response.body().getExtra().getAvito().getName(), resources.getString(R.string.responseHandler_avito_name), true);
                        result = addStringIfNotEmpty(result, response.body().getExtra().getAvito().getAddress(), resources.getString(R.string.responseHandler_avito_adress), true);
                    }
                }
                if(!response.body().getComments().isEmpty()){
                    result = addStringIfNotEmpty(result,response.body().getComments(),resources.getString(R.string.responseHandler_comment),false);
                }
                numberInfoCallbacks.onGetNumberInfo(result);
            }

            @Override
            public void onGetFailed(Throwable error) {
                numberInfoCallbacks.onGetNumberInfoFailed(error);
            }
        };

        NetworkRequests networkRequests = new NetworkRequests(URL);
        networkRequests.MakeRequest(number, makeRequestCallbacks);
    }

    private String addStringIfNotEmpty(String result,String varible, String varibleHeader, Boolean chageTheLine ){

        if (!varible.isEmpty()){
            result +=varibleHeader + "  " + varible;
            if(chageTheLine){
                result += "\n";
            }
        }

        return result;
    }

    private String addStringIfNotEmpty(String result, List<String> varible, String varibleHeader, Boolean chageTheLine){

        if (!varible.isEmpty()){
            result +=varibleHeader + "  " + varible.toString();
            if(chageTheLine){
                result += "\n";
            }
        }

        return result;
    }
}
