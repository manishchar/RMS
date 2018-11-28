package alina.com.rms.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMastDatum {

@SerializedName("station_id")
@Expose
private String stationId;
@SerializedName("station_name")
@Expose
private String stationName;
@SerializedName("station_km")
@Expose
private String stationKm;
@SerializedName("mast")
@Expose
private String mast;
@SerializedName("mast_id")
@Expose
private String mastId;

@SerializedName("mast_prog_id")
@Expose
private String mast_prog_id="";

    @SerializedName("mast_prog_no")
    @Expose
    private String mast_prog_no="";

    @SerializedName("add_img")
    @Expose
    private Bitmap bitmap;

    private String lat;

    private String lng;

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


    public String getMast_prog_no() {
        return mast_prog_no;
    }

    public void setMast_prog_no(String mast_prog_no) {
        this.mast_prog_no = mast_prog_no;
    }

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

public String getMast() {
return mast;
}

public void setMast(String mast) {
this.mast = mast;
}

public String getMastId() {
return mastId;
}

public void setMastId(String mastId) {
this.mastId = mastId;
}

    public String getMast_prog_id() {
        return mast_prog_id;
    }

    public void setMast_prog_id(String mast_prog_id) {
        this.mast_prog_id = mast_prog_id;
    }


}