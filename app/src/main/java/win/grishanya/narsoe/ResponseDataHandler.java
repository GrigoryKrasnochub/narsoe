package win.grishanya.narsoe;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import win.grishanya.narsoe.dataClasses.ExpandedRecyclerViewData;
import win.grishanya.narsoe.dataClasses.ExpandedViewsWrapperData;
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

    public interface NumberInfoFullInExpandedViewsWrapperCallbacks{
        void onGetNumberInfo(ArrayList<ExpandedViewsWrapperData> result);
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

    public void getFullNumberInfoInExpandedViewsWrapper (String number, final Resources resources, final NumberInfoFullInExpandedViewsWrapperCallbacks numberInfoCallbacks){
        NetworkRequests.MakeRequestCallbacks makeRequestCallbacks = new NetworkRequests.MakeRequestCallbacks() {
            @Override
            public void onGetResponse(Response<InfoListShort> response) {
                ArrayList<ExpandedViewsWrapperData> result = new ArrayList<>();

                ExpandedViewsWrapperData expandedViewsWrapperGeneral = new ExpandedViewsWrapperData();
                expandedViewsWrapperGeneral.Header = resources.getString(R.string.number_info_activity_general);
                expandedViewsWrapperGeneral.Description = resources.getString(R.string.number_info_activity_general_info_description);
                expandedViewsWrapperGeneral.Expanded = true;
                ArrayList<ExpandedRecyclerViewData> infoGeneral = new ArrayList<>();

                addDataToArrayList(infoGeneral,response.body().getName(),resources.getString(R.string.responseHandler_name));
                addDataToArrayList(infoGeneral,String.valueOf(response.body().getRating()),resources.getString(R.string.responseHandler_rating));
                addDataToArrayList(infoGeneral,response.body().getType(),resources.getString(R.string.responseHandler_type));
                addDataToArrayList(infoGeneral,response.body().getRegion(),resources.getString(R.string.responseHandler_region));
                if(response.body().getCompany() != null) {
                    addDataToArrayList(infoGeneral,response.body().getCompany().getName(),resources.getString(R.string.responseHandler_company_name));
                    addDataToArrayList(infoGeneral,response.body().getCompany().getDescription(),resources.getString(R.string.responseHandler_company_description));
                    addDataToArrayList(infoGeneral,response.body().getCompany().getCity(),resources.getString(R.string.responseHandler_company_city));
                    addDataToArrayList(infoGeneral,response.body().getCompany().getUrl(),resources.getString(R.string.responseHandler_company_url));
                    addDataToArrayList(infoGeneral,response.body().getCompany().getTelephone(),resources.getString(R.string.responseHandler_company_telephone));
                    addDataToArrayList(infoGeneral,response.body().getCompany().getAddress(),resources.getString(R.string.responseHandler_company_adress));
                    addDataToArrayList(infoGeneral,response.body().getCompany().getEmail(),resources.getString(R.string.responseHandler_company_email));
                }

                expandedViewsWrapperGeneral.data = infoGeneral;
                result.add(expandedViewsWrapperGeneral);

                ExpandedViewsWrapperData expandedViewsWrapperExtra = new ExpandedViewsWrapperData();
                expandedViewsWrapperExtra.Header = resources.getString(R.string.number_info_activity_extra);
                expandedViewsWrapperExtra.Description = resources.getString(R.string.number_info_activity_extra_info_description);
                ArrayList<ExpandedRecyclerViewData> infoExtra = new ArrayList<>();

                if(response.body().getExtra() != null) {
                    if(response.body().getExtra().getAvito() != null) {
                        addDataToArrayList(infoExtra, response.body().getExtra().getAvito().getName(), resources.getString(R.string.responseHandler_avito_name));
                        addDataToArrayList(infoExtra, response.body().getExtra().getAvito().getAddress(), resources.getString(R.string.responseHandler_avito_adress));
                    }
                    expandedViewsWrapperExtra.data = infoExtra;
                    result.add(expandedViewsWrapperExtra);
                }

                ExpandedViewsWrapperData expandedViewsWrapperComments = new ExpandedViewsWrapperData();
                expandedViewsWrapperComments.Header = resources.getString(R.string.number_info_activity_comments);
                expandedViewsWrapperComments.Description = resources.getString(R.string.number_info_activity_comments_info_description);
                ArrayList<ExpandedRecyclerViewData> infoComments = new ArrayList<>();

                if(!response.body().getComments().isEmpty()){
                    addDataToArrayList(infoComments,response.body().getComments(),resources.getString(R.string.responseHandler_comment));
                    expandedViewsWrapperComments.data = infoComments;
                    result.add(expandedViewsWrapperComments);
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

    private ArrayList<ExpandedRecyclerViewData> addDataToArrayList(ArrayList<ExpandedRecyclerViewData> result,String variable,String header){
        if(!variable.isEmpty()){
             ExpandedRecyclerViewData expandedRecyclerViewData = new ExpandedRecyclerViewData();
             expandedRecyclerViewData.Header = header;
             expandedRecyclerViewData.Info = variable;
            result.add(expandedRecyclerViewData);
        }
        return result;
    }

    private ArrayList<ExpandedRecyclerViewData> addDataToArrayList(ArrayList<ExpandedRecyclerViewData> result,List<String> variable,String header){
        if(!variable.isEmpty()){
            ExpandedRecyclerViewData expandedRecyclerViewData = new ExpandedRecyclerViewData();
            expandedRecyclerViewData.Header = header;
            StringBuilder sb = new StringBuilder(variable.get(0));
            variable.remove(0);
            for (String var: variable){
                sb.append(",");
                sb.append(var);
            }
            expandedRecyclerViewData.Info = sb.toString();
            result.add(expandedRecyclerViewData);
        }
        return result;
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
