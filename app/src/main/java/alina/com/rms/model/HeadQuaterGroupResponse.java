package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HeadQuaterGroupResponse {

@SerializedName("response_code")
@Expose
private int responseCode;
@SerializedName("message")
@Expose
private String message;
@SerializedName("grouplist")
@Expose
private List<Grouplist> grouplist = null;

    @SerializedName("target")
    @Expose
    private List<Target> target = null;

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

public List<Grouplist> getGrouplist() {
return grouplist;
}

public void setGrouplist(List<Grouplist> grouplist) {
this.grouplist = grouplist;
}

    public List<Target> getTarget() {
        return target;
    }

    public void setTarget(List<Target> target) {
        this.target = target;
    }

}