package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Target {

@SerializedName("target")
@Expose
private String target;
@SerializedName("eigdate")
@Expose
private String eigdate;
@SerializedName("crsdate")
@Expose
private String crsdate;

@SerializedName("targetvalue")
@Expose
private Targetvalue targetvalue;

@SerializedName("tsstargetvalue")
@Expose
private Tsstargetvalue tsstargetvalue;


@SerializedName("sptargetvalue")
@Expose
private Sptargetvalue sptargetvalue;


@SerializedName("ssptargetvalue")
@Expose
private Sptargetvalue ssptargetvalue;

public Sptargetvalue getSptargetvalue() {
    return sptargetvalue;
}

public void setSsptargetvalue(Sptargetvalue sptargetvalue) {
    this.sptargetvalue = sptargetvalue;
}


    public Sptargetvalue getSsptargetvalue() {
        return ssptargetvalue;
    }

    public void setSptargetvalue(Sptargetvalue ssptargetvalue) {
        this.ssptargetvalue = ssptargetvalue;
    }





public Tsstargetvalue getTsstargetvalue() {
    return tsstargetvalue;
}

public void setTsstargetvalue(Tsstargetvalue tsstargetvalue) {
    this.tsstargetvalue = tsstargetvalue;
}

public String getTarget() {
return target;
}

public void setTarget(String target) {
this.target = target;
}

public String getEigdate() {
return eigdate;
}

public void setEigdate(String eigdate) {
this.eigdate = eigdate;
}

public String getCrsdate() {
return crsdate;
}

public void setCrsdate(String crsdate) {
this.crsdate = crsdate;
}

    public Targetvalue getTargetvalue() {
        return targetvalue;
    }

    public void setTargetvalue(Targetvalue targetvalue) {
        this.targetvalue = targetvalue;
    }

}