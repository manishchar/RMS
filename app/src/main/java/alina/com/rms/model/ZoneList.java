package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoneList {

@SerializedName("zone")
@Expose
private String zone;

public String getZone() {
return zone;
}

public void setZone(String zone) {
this.zone = zone;
}

}