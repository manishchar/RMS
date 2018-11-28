package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

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
@SerializedName("user_type")
@Expose
private Integer user_type;


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

public int getRole() {
return role;
}

public void setRole(Integer role) {
this.role = role;
}

    public Integer getUser_type() {
        return user_type;
    }

    public void setUser_type(Integer user_type) {
        this.user_type = user_type;
    }

}


