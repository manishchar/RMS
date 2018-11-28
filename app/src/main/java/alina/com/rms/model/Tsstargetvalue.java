package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tsstargetvalue {

@SerializedName("Earth-filling-work")
@Expose
private String earthFillingWork;
@SerializedName("Building-work")
@Expose
private String buildingWork;
@SerializedName("Foundation")
@Expose
private String foundation;
@SerializedName("Structure-erection")
@Expose
private String structureErection;
@SerializedName("Equipment-erection")
@Expose
private String equipmentErectio;
@SerializedName("Transformer-erection")
@Expose
private String transformerErection;
@SerializedName("Earth-mat")
@Expose
private String earthMat;
@SerializedName("Cabling")
@Expose
private String cabling;
@SerializedName("Testing")
@Expose
private String testing;
@SerializedName("Commissioning")
@Expose
private String commissioning;

public String getEarthFillingWork() {
return earthFillingWork;
}

public void setEarthFillingWork(String earthFillingWork) {
this.earthFillingWork = earthFillingWork;
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

public String getStructureErection() {
return structureErection;
}

public void setStructureErection(String structureErection) {
this.structureErection = structureErection;
}

public String getEquipmentErectio() {
return equipmentErectio;
}

public void setEquipmentErectio(String equipmentErectio) {
this.equipmentErectio = equipmentErectio;
}

public String getTransformerErection() {
return transformerErection;
}

public void setTransformerErection(String transformerErection) {
this.transformerErection = transformerErection;
}

public String getEarthMat() {
return earthMat;
}

public void setEarthMat(String earthMat) {
this.earthMat = earthMat;
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