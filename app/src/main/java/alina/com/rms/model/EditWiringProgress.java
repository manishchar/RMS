package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditWiringProgress {

    @SerializedName("project_name")
    @Expose
    private String projectName;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("section_name")
    @Expose
    private String sectionName;
    @SerializedName("wiring_prog_no")
    @Expose
    private String wiringProgNo;
    @SerializedName("wiring_date")
    @Expose
    private String wiringDate;
    @SerializedName("wiring_prog_id")
    @Expose
    private String wiringProgId;
    @SerializedName("station_name")
    @Expose
    private String stationName;
    @SerializedName("station_id")
    @Expose
    private String stationId;
    @SerializedName("station_km")
    @Expose
    private String stationKm;
    @SerializedName("wiring")
    @Expose
    private String wiring;
    @SerializedName("img")
    @Expose
    private String img;


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getWiringProgNo() {
        return wiringProgNo;
    }

    public void setWiringProgNo(String wiringProgNo) {
        this.wiringProgNo = wiringProgNo;
    }

    public String getWiringDate() {
        return wiringDate;
    }

    public void setWiringDate(String wiringDate) {
        this.wiringDate = wiringDate;
    }

    public String getWiringProgId() {
        return wiringProgId;
    }

    public void setWiringProgId(String wiringProgId) {
        this.wiringProgId = wiringProgId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationKm() {
        return stationKm;
    }

    public void setStationKm(String stationKm) {
        this.stationKm = stationKm;
    }

    public String getWiring() {
        return wiring;
    }

    public void setWiring(String wiring) {
        this.wiring = wiring;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}