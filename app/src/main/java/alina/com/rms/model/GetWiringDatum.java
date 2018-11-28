package alina.com.rms.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetWiringDatum {

@SerializedName("station_id")
@Expose
private String stationId;
@SerializedName("station_name")
@Expose
private String stationName;
@SerializedName("station_km")
@Expose
private String stationKm;
@SerializedName("wiring")
@Expose
private String wiring;
@SerializedName("wiring_id")
@Expose
private String wiringId;

    @SerializedName("wiring_prog_no")
    @Expose
    private String wiring_prog_no="";


    @SerializedName("add_img")
    @Expose
    private Bitmap bitmap;

    private String lat;

    private String lng;

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

public String getStationKm() {
return stationKm;
}

public void setStationKm(String stationKm) {
this.stationKm = stationKm;
}

public String getWiring() {
return wiring;
}

public void setWiring(String wiring) {
this.wiring = wiring;
}

public String getWiringId() {
return wiringId;
}

public void setWiringId(String wiringId) {
this.wiringId = wiringId;
}

    public String getWiring_prog_no() {
        return wiring_prog_no;
    }

    public void setWiring_prog_no(String wiring_prog_no) {
        this.wiring_prog_no = wiring_prog_no;
    }



    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}