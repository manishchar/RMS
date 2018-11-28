package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditMastProgress {

@SerializedName("project_name")
@Expose
private String projectName;
@SerializedName("group_name")
@Expose
private String groupName;
@SerializedName("section_name")
@Expose
private String sectionName;
@SerializedName("mast_prog_no")
@Expose
private String mastProgNo;
@SerializedName("mast_date")
@Expose
private String mastDate;
@SerializedName("mast_prog_id")
@Expose
private String mastProgId;
@SerializedName("station_name")
@Expose
private String stationName;
@SerializedName("station_id")
@Expose
private String stationId;
@SerializedName("station_km")
@Expose
private String stationKm;
@SerializedName("mast")
@Expose
private String mast;

    @SerializedName("img")
    @Expose
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

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

public String getMastProgNo() {
return mastProgNo;
}

public void setMastProgNo(String mastProgNo) {
this.mastProgNo = mastProgNo;
}

public String getMastDate() {
return mastDate;
}

public void setMastDate(String mastDate) {
this.mastDate = mastDate;
}

public String getMastProgId() {
return mastProgId;
}

public void setMastProgId(String mastProgId) {
this.mastProgId = mastProgId;
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

public String getMast() {
return mast;
}

public void setMast(String mast) {
this.mast = mast;
}

}