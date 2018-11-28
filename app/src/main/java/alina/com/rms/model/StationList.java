package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StationList {

@SerializedName("station_id")
@Expose
private String stationId;
@SerializedName("station_name")
@Expose
private String stationName;

public String getStationId() {
return stationId;
}

public void setStationId(String stationId) {
this.stationId = stationId;
}

public String getStationName() {
return stationName;
}

public void setStationName(String stationName) {
this.stationName = stationName;
}

}