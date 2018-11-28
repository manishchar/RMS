package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DivisionList {

@SerializedName("division")
@Expose
private String division;

public String getDivision() {
return division;
}

public void setDivision(String division) {
this.division = division;
}

}