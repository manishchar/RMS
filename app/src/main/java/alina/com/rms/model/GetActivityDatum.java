package alina.com.rms.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetActivityDatum {

    @SerializedName("station_id")
    @Expose
    private String stationId;
    @SerializedName("station_name")
    @Expose
    private String stationName;
    @SerializedName("station_km")
    @Expose
    private String stationKm;
    @SerializedName("activity")
    @Expose
    private String activity;
    @SerializedName("id")
    @Expose
    private String id;
    private String _prog_no;
    private String tss_name="";
    @SerializedName("add_img")
    @Expose
    private Bitmap bitmap;

    private String lat;

    private String lng;

    private String user_id;

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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void set_prog_no(String _prog_no) {
        this._prog_no = _prog_no;
    }

    public String get_prog_no() {
        return _prog_no;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTss_name() {
        return tss_name;
    }

    public void setTss_name(String tss_name) {
        this.tss_name = tss_name;
    }
}
