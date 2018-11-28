package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Headquateruserlist implements Serializable {

@SerializedName("id")
@Expose
private String id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("headquarter")
@Expose
private String headquarter;
@SerializedName("headquarter_id")
@Expose
private String headquarterId;
@SerializedName("email")
@Expose
private String email;
@SerializedName("password")
@Expose
private String password;
@SerializedName("mobile")
@Expose
private String mobile;

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

public String getHeadquarter() {
return headquarter;
}

public void setHeadquarter(String headquarter) {
this.headquarter = headquarter;
}

public String getHeadquarterId() {
return headquarterId;
}

public void setHeadquarterId(String headquarterId) {
this.headquarterId = headquarterId;
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

}