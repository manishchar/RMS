package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupResponse {

@SerializedName("response_code")
@Expose
private Integer responseCode;
@SerializedName("message")
@Expose
private String message;
@SerializedName("response")
@Expose
private List<GroupResponseOfResponse> response = null;

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

public List<GroupResponseOfResponse> getResponse() {
return response;
}

public void setResponse(List<GroupResponseOfResponse> response) {
this.response = response;
}

}