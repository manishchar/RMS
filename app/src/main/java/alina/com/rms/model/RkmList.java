package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RkmList implements Serializable {


    @SerializedName("crs_id")
    @Expose
    private String crs_id;
    @SerializedName("proj_id")
    @Expose
    private String projId;
    @SerializedName("proj_name")
    @Expose
    private String projName;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("division")
    @Expose
    private String division;

    @SerializedName("org_name")
    @Expose
    private String org_name;

    @SerializedName("section_name")
    @Expose
    private String section_name;

    @SerializedName("month")
    @Expose
    private String month;

    @SerializedName("rkm_id")
    @Expose
    private String rkmId;
    @SerializedName("rkm")
    @Expose
    private String rkm;
    @SerializedName("traget")
    @Expose
    private String traget;
    @SerializedName("division_id")
    @Expose
    private String divisionId;
    @SerializedName("division_name")
    @Expose
    private String divisionName;
    @SerializedName("zone")
    @Expose
    private String zone;
    @SerializedName("zone_id")
    @Expose
    private String zoneId;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("crs")
    @Expose
    private String crs;

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
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

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }


    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }


    public String getRkmId() {
        return rkmId;
    }

    public void setRkmId(String rkmId) {
        this.rkmId = rkmId;
    }


    public String getTraget() {
        return traget;
    }

    public void setTraget(String traget) {
        this.traget = traget;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }


    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }


    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    public String getCrs_id() {
        return crs_id;
    }

    public void setCrs_id(String crs_id) {
        this.crs_id = crs_id;
    }
}