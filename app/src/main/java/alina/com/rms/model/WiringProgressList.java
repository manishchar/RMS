package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WiringProgressList {

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

    @SerializedName("typename")
    @Expose
    private String typename;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("station_id")
    @Expose
    private String station_id;

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

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }
}