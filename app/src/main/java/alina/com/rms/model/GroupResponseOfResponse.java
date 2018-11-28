package alina.com.rms.model;

/**
 * Created by alina on 14-01-2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupResponseOfResponse {

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
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
