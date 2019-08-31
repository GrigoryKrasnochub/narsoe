package win.grishanya.narsoe.dataClasses.InfoListShort_Extras;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Avito {

    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Name")
    @Expose
    private String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
