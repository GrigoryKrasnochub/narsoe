package win.grishanya.narsoe.dataClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoRating {

    @SerializedName("Rating")
    @Expose
    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
