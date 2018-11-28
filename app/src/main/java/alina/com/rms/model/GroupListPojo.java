package alina.com.rms.model;

/**
 * Created by alina on 17-03-2018.
 */
import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class GroupListPojo {

    @SerializedName("group_name")
    @Expose
    private String groupName;

    @SerializedName("group_id")
    @Expose
    private String group_id;

    public String getGroupName() {
        return groupName;
    }

    /**/
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

}
