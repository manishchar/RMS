package alina.com.rms.model;

/**
 * Created by HP on 02-01-2018.
 */

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class LoginResultPojo {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("useremail")
    @Expose
    private String useremail;
    @SerializedName("headquater")
    @Expose
    private String headquater;
    @SerializedName("group")
    @Expose
    private String group;


    @SerializedName("role")
    @Expose
    private Integer role;

    @SerializedName("section_id")
    @Expose
    private String section_id;

    @SerializedName("user_type")
    @Expose
    private Integer user_type;

    private String date;

    private String ohetype;

    private String target;

    private String master_id;


    public String getMaster_id() {
        return master_id;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getHeadquater() {
        return headquater;
    }

    public void setHeadquater(String headquater) {
        this.headquater = headquater;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOhetype() {
        return ohetype;
    }

    public void setOhetype(String ohetype) {
        this.ohetype = ohetype;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSectionId() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public Integer getUser_type() {
        return user_type;
    }

    public void setUser_type(Integer user_type) {
        this.user_type = user_type;
    }

}