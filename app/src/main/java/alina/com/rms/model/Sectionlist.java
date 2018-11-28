package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sectionlist implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("headquarter")
    @Expose
    private String headquarter;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("agency")
    @Expose
    private String agency;
    @SerializedName("crs")
    @Expose
    private String crs;
    @SerializedName("targete")
    @Expose
    private String targete;

    @SerializedName("rkm")
    @Expose
    private String rkm;
    @SerializedName("tkm")
    @Expose
    private String tkm;

    @SerializedName("tss_name")
    @Expose
    private String tss_name;
    @SerializedName("ssp_name")
    @Expose
    private String ssp_name;
    @SerializedName("sp_name")
    @Expose
    private String sp_name;

    @SerializedName("section_id")
    @Expose
    private String section_id;

    public String getTss_name() {
        return tss_name;
    }

    public void setTss_name(String tss_name) {
        this.tss_name = tss_name;
    }

    public String getSsp_name() {
        return ssp_name;
    }

    public void setSsp_name(String ssp_name) {
        this.ssp_name = ssp_name;
    }

    public String getSp_name() {
        return sp_name;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getHeadquarter() {
        return headquarter;
    }

    public void setHeadquarter(String headquarter) {
        this.headquarter = headquarter;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    public String getTargete() {
        return targete;
    }

    public void setTargete(String targete) {
        this.targete = targete;
    }

    public String getRkm() {
        return rkm;
    }

    public void setRkm(String rkm) {
        this.rkm = rkm;
    }

    public String getTkm() {
        return tkm;
    }

    public void setTkm(String tkm) {
        this.tkm = tkm;
    }

}
