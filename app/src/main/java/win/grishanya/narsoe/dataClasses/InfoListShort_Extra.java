package win.grishanya.narsoe.dataClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import win.grishanya.narsoe.dataClasses.InfoListShort_Extras.Avito;

public class InfoListShort_Extra {

    @SerializedName("Avito")
    @Expose
    private Avito avito;

    public Avito getAvito() {
        return avito;
    }

    public void setAvito(Avito avito) {
        this.avito = avito;
    }

}
