package alina.com.rms.model;

/**
 * Created by alina on 17-03-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PublicModuleResponseModel {

    @SerializedName("response_code")
    @Expose
    private int responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("list")
    @Expose
    private List<OrgnaizationListPojo> list = null;
    @SerializedName("groupList")
    @Expose
    private List<GroupListPojo> groupList = null;
    @SerializedName("sectionList")
    @Expose
    private List<SectionListPojo> sectionList = null;

    @SerializedName("stateList")
    @Expose
    private List<StateList> stateList = null;

    @SerializedName("zoneList")
    @Expose
    private List<ZoneList> zoneList = null;

    @SerializedName("divisionList")
    @Expose
    private List<DivisionList> divisionList = null;

    @SerializedName("stationList")
    @Expose
    private List<StationList> stationList = null;

    @SerializedName("stationlist")
    @Expose
    private List<Stationlist1> stationlist = null;

    @SerializedName("datalist")
    @Expose
    private List<Datalist> datalist = null;

    public List<DivisionList> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(List<DivisionList> divisionList) {
        this.divisionList = divisionList;
    }

    public List<StateList> getStateList() {
        return stateList;
    }

    public void setStateList(List<StateList> stateList) {
        this.stateList = stateList;
    }

    public List<SectionListPojo> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<SectionListPojo> sectionList) {
        this.sectionList = sectionList;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public java.util.List<OrgnaizationListPojo> getList() {
        return list;
    }

    public void setList(List<OrgnaizationListPojo> list) {
        this.list = list;
    }

    public List<GroupListPojo> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupListPojo> groupList) {
        this.groupList = groupList;
    }


    public List<ZoneList> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<ZoneList> zoneList) {
        this.zoneList = zoneList;
    }

    public List<StationList> getStationList() {
        return stationList;
    }

    public void setStationList(List<StationList> stationList) {
        this.stationList = stationList;
    }

    public List<Stationlist1> getStationlist() {
        return stationlist;
    }

    public void setStationlist(List<Stationlist1> stationlist) {
        this.stationlist = stationlist;
    }

    public List<Datalist> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<Datalist> datalist) {
        this.datalist = datalist;
    }
}


