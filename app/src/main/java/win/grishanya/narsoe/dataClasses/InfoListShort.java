package win.grishanya.narsoe.dataClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InfoListShort {

    @SerializedName("Rating")
    @Expose
    private int rating;
    @SerializedName("Extra")
    @Expose
    private InfoListShort_Extra extra;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Region")
    @Expose
    private String region;
    @SerializedName("Comments")
    @Expose
    private List<String> comments = null;
    @SerializedName("Company")
    @Expose
    private InfoListShort_Company company;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public InfoListShort_Company getCompany() {
        return company;
    }

    public void setCompany(InfoListShort_Company company) {
        this.company = company;
    }

    public InfoListShort_Extra getExtra() {
        return extra;
    }

    public void setExtra(InfoListShort_Extra extra) {
        this.extra = extra;
    }
}
