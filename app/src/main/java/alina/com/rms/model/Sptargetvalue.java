package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sptargetvalue {

@SerializedName("Civil-Earth-work")
@Expose
private String civilEarthWork;
@SerializedName("Building-work")
@Expose
private String buildingWork;
@SerializedName("Eqpt-foundation")
@Expose
private String foundation;
@SerializedName("Eqpt-erection")
@Expose
private String eqptErection;
@SerializedName("Cabling")
@Expose
private String cabling;
@SerializedName("Testing")
@Expose
private String testing;
@SerializedName("Commissioning")
@Expose
private String commissioning;

public String getCivilEarthWork() {
return civilEarthWork;
}

public void setCivilEarthWork(String civilEarthWork) {
this.civilEarthWork = civilEarthWork;
}

public String getBuildingWork() {
return buildingWork;
}

public void setBuildingWork(String buildingWork) {
this.buildingWork = buildingWork;
}

public String getFoundation() {
return foundation;
}

public void setFoundation(String foundation) {
this.foundation = foundation;
}

public String getEqptErection() {
return eqptErection;
}

public void setEqptErection(String eqptErection) {
this.eqptErection = eqptErection;
}

public String getCabling() {
return cabling;
}

public void setCabling(String cabling) {
this.cabling = cabling;
}

public String getTesting() {
return testing;
}

public void setTesting(String testing) {
this.testing = testing;
}

public String getCommissioning() {
return commissioning;
}

public void setCommissioning(String commissioning) {
this.commissioning = commissioning;
}

}
