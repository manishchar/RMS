package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Userlist implements Serializable {

@SerializedName("id")
@Expose
private String id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("headquater")
@Expose
private String headquater;
@SerializedName("group")
@Expose
private String group;
@SerializedName("headquater_id")
@Expose
private String headquaterId;
@SerializedName("group_id")
@Expose
private String groupId;
@SerializedName("email")
@Expose
private String email;
@SerializedName("password")
@Expose
private String password;
@SerializedName("mobile")
@Expose
private String mobile;


@SerializedName("usertype")
@Expose
private int usertype;


public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
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

public String getHeadquaterId() {
return headquaterId;
}

public void setHeadquaterId(String headquaterId) {
this.headquaterId = headquaterId;
}

public String getGroupId() {
return groupId;
}

public void setGroupId(String groupId) {
this.groupId = groupId;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getPassword() {
return password;
}

public void setPassword(String password) {
this.password = password;
}

public String getMobile() {
return mobile;
}

public void setMobile(String mobile) {
this.mobile = mobile;
}

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

}