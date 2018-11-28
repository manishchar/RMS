package alina.com.rms.model;

/**
 * Created by alina on 13-01-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private List<Response> response = null;
    @SerializedName("headquaterlist")
    @Expose
    private List<Headquaterlist> headquaterlist = null;

    @SerializedName("headquateruserlist")
    @Expose
    private List<Headquateruserlist> headquateruserlist = null;

    @SerializedName("userlist")
    @Expose
    private List<Userlist> userlist = null;

    @SerializedName("grouplist")
    @Expose
    private List<Userlist> grouplist = null;

    @SerializedName("sectionlist")
    @Expose
    private List<Sectionlist> sectionlist = null;

    @SerializedName("rkmList")
    @Expose
    private List<RkmList> rkmList = null;

    @SerializedName("projectList")
    @Expose
    private List<ProjectList> projectList = null;
    @SerializedName("crsList")
    @Expose
    private List<CrsList> crsList = null;


    @SerializedName("getFoundationData")
    @Expose
    private List<GetFoundationDatum> getFoundationData = null;

    @SerializedName("foundationProgressList")
    @Expose
    private List<FoundationProgressList> foundationProgressList = null;


    @SerializedName("editProgress")
    @Expose
    private List<EditProgress> editProgress = null;

    @SerializedName("mastProgressList")
    @Expose
    private List<MastProgressList> mastProgressList = null;
    @SerializedName("getMastData")
    @Expose
    private List<GetMastDatum> getMastData = null;

    @SerializedName("editMastProgress")
    @Expose
    private List<EditMastProgress> editMastProgress = null;

    @SerializedName("activityProgressList")
    @Expose
    private List<WiringProgressList> activityProgressList = null;


    @SerializedName("wiringProgressList")
    @Expose
    private List<WiringProgressList> wiringProgressList = null;

    @SerializedName("getWiringData")
    @Expose
    private List<GetWiringDatum> getWiringData = null;

    @SerializedName("editWiringProgress")
    @Expose
    private List<EditWiringProgress> editWiringProgress = null;

    @SerializedName("getActivityData")
    @Expose
    private List<GetActivityDatum> getActivityData = null;



    @SerializedName("gettssActivity")
    @Expose
    private List<GetActivityDatum> gettssActivity = null;

    public List<GetActivityDatum> getGetActivityData() {
        return getActivityData;
    }

    public void setGetActivityData(List<GetActivityDatum> getActivityData) {
        this.getActivityData = getActivityData;
    }

    public List<GetWiringDatum> getGetWiringData() {
        return getWiringData;
    }

    public void setGetWiringData(List<GetWiringDatum> getWiringData) {
        this.getWiringData = getWiringData;
    }

    public List<WiringProgressList> getWiringProgressList() {
        return wiringProgressList;
    }

    public void setWiringProgressList(List<WiringProgressList> wiringProgressList) {
        this.wiringProgressList = wiringProgressList;
    }

    public List<WiringProgressList> getActivityProgressList() {
        return activityProgressList;
    }

    public void setActivityProgressList(List<WiringProgressList> activityProgressList) {
        this.activityProgressList = activityProgressList;
    }

    public List<EditMastProgress> getEditMastProgress() {
        return editMastProgress;
    }

    public void setEditMastProgress(List<EditMastProgress> editMastProgress) {
        this.editMastProgress = editMastProgress;
    }

    public List<GetMastDatum> getGetMastData() {
        return getMastData;
    }

    public void setGetMastData(List<GetMastDatum> getMastData) {
        this.getMastData = getMastData;
    }


    public List<MastProgressList> getMastProgressList() {
        return mastProgressList;
    }

    public void setMastProgressList(List<MastProgressList> mastProgressList) {
        this.mastProgressList = mastProgressList;
    }

    public List<EditProgress> getEditProgress() {
        return editProgress;
    }

    public void setEditProgress(List<EditProgress> editProgress) {
        this.editProgress = editProgress;
    }


    public List<FoundationProgressList> getFoundationProgressList() {
        return foundationProgressList;
    }

    public void setFoundationProgressList(List<FoundationProgressList> foundationProgressList) {
        this.foundationProgressList = foundationProgressList;
    }



    public List<GetFoundationDatum> getGetFoundationData() {
        return getFoundationData;
    }

    public void setGetFoundationData(List<GetFoundationDatum> getFoundationData) {
        this.getFoundationData = getFoundationData;
    }

    public List<CrsList> getCrsList() {
        return crsList;
    }

    public void setCrsList(List<CrsList> crsList) {
        this.crsList = crsList;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public List<Headquaterlist> getHeadquaterlist() {
        return headquaterlist;
    }

    public void setHeadquaterlist(List<Headquaterlist> headquaterlist) {
        this.headquaterlist = headquaterlist;
    }
    public List<ProjectList> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectList> projectList) {
        this.projectList = projectList;
    }

    public List<Headquateruserlist> getHeadquateruserlist() {
        return headquateruserlist;
    }

    public void setHeadquateruserlist(List<Headquateruserlist> headquateruserlist) {
        this.headquateruserlist = headquateruserlist;
    }


    public List<Userlist> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<Userlist> userlist) {
        this.userlist = userlist;
    }


    public List<Userlist> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(List<Userlist> grouplist) {
        this.grouplist = grouplist;
    }

    public List<Sectionlist> getSectionlist() {
        return sectionlist;
    }

    public void setSectionlist(List<Sectionlist> sectionlist) {
        this.sectionlist = sectionlist;
    }

    public List<RkmList> getRkmList() {
        return rkmList;
    }

    public void setRkmList(List<RkmList> rkmList) {
        this.rkmList = rkmList;
    }

    public List<EditWiringProgress> getEditWiringProgress() {
        return editWiringProgress;
    }

    public void setEditWiringProgress(List<EditWiringProgress> editWiringProgress) {
        this.editWiringProgress = editWiringProgress;
    }

    public List<GetActivityDatum> getGettssActivity() {
        return gettssActivity;
    }

    public void setGettssActivity(List<GetActivityDatum> gettssActivity) {
        this.gettssActivity = gettssActivity;
    }
}