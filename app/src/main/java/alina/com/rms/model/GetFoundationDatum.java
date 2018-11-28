package alina.com.rms.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFoundationDatum {

@SerializedName("station_id")
@Expose
private String stationId;
@SerializedName("station_name")
@Expose
private String stationName;
@SerializedName("station_km")
@Expose
private String stationKm;
@SerializedName("foundation")
@Expose
private String foundation;
@SerializedName("foundation_id")
@Expose
private String foundationId;

    @SerializedName("foundation_prog_no")
    @Expose
    private String foundation_prog_no="";

    @SerializedName("foundation_prog1__id")
    @Expose
    private String foundation_prog1__id;

    @SerializedName("month")
    @Expose
    private String month;

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

public String getFoundation() {
return foundation;
}

public void setFoundation(String foundation) {
this.foundation = foundation;
}

public String getFoundationId() {
return foundationId;
}

public void setFoundationId(String foundationId) {
this.foundationId = foundationId;
}

    public String getFoundation_prog_no() {
        return foundation_prog_no;
    }

    public void setFoundation_prog_no(String foundation_prog_no) {
        this.foundation_prog_no = foundation_prog_no;
    }

    public String getFoundation_prog1__id() {
        return foundation_prog1__id;
    }

    public void setFoundation_prog1__id(String foundation_prog1__id) {
        this.foundation_prog1__id = foundation_prog1__id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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