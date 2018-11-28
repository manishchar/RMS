package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrsList {

@SerializedName("crs_id")
@Expose
private String crsId;
@SerializedName("org_name")
@Expose
private String orgName;
@SerializedName("proj_name")
@Expose
private String projName;
@SerializedName("group_name")
@Expose
private String groupName;
@SerializedName("group_id")
@Expose
private String groupId;
@SerializedName("section_id")
@Expose
private String sectionId;
@SerializedName("section_name")
@Expose
private String sectionName;
@SerializedName("division")
@Expose
private String division;
@SerializedName("division_name")
@Expose
private String divisionName;
@SerializedName("zone")
@Expose
private String zone;
@SerializedName("zone_name")
@Expose
private String zoneName;
@SerializedName("state_id")
@Expose
private String stateId;
@SerializedName("state")
@Expose
private String state;
@SerializedName("rkm")
@Expose
private String rkm;
@SerializedName("target")
@Expose
private String target;
@SerializedName("crs")
@Expose
private String crs;
@SerializedName("month")
@Expose
private String month;

public String getCrsId() {
return crsId;
}

public void setCrsId(String crsId) {
this.crsId = crsId;
}

public String getOrgName() {
return orgName;
}

public void setOrgName(String orgName) {
this.orgName = orgName;
}

public String getProjName() {
return projName;
}

public void setProjName(String projName) {
this.projName = projName;
}

public String getGroupName() {
return groupName;
}

public void setGroupName(String groupName) {
this.groupName = groupName;
}

public String getGroupId() {
return groupId;
}

public void setGroupId(String groupId) {
this.groupId = groupId;
}

public String getSectionId() {
return sectionId;
}

public void setSectionId(String sectionId) {
this.sectionId = sectionId;
}

public String getSectionName() {
return sectionName;
}

public void setSectionName(String sectionName) {
this.sectionName = sectionName;
}

public String getDivision() {
return division;
}

public void setDivision(String division) {
this.division = division;
}

public String getDivisionName() {
return divisionName;
}

public void setDivisionName(String divisionName) {
this.divisionName = divisionName;
}

public String getZone() {
return zone;
}

public void setZone(String zone) {
this.zone = zone;
}

public String getZoneName() {
return zoneName;
}

public void setZoneName(String zoneName) {
this.zoneName = zoneName;
}

public String getStateId() {
return stateId;
}

public void setStateId(String stateId) {
this.stateId = stateId;
}

public String getState() {
return state;
}

public void setState(String state) {
this.state = state;
}

public String getRkm() {
return rkm;
}

public void setRkm(String rkm) {
this.rkm = rkm;
}

public String getTarget() {
return target;
}

public void setTarget(String target) {
this.target = target;
}

public String getCrs() {
return crs;
}

public void setCrs(String crs) {
this.crs = crs;
}

public String getMonth() {
return month;
}

public void setMonth(String month) {
this.month = month;
}

}