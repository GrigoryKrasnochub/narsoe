package win.grishanya.narsoe.dataClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InfoListShort_Avito {
    @SerializedName("Address")
    @Expose
    private String address = null;
    @SerializedName("Name")
    @Expose
    private String name = null;

    public String getAddress(){return address;}
    public void setAddress(String address){this.address = address;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}
}
