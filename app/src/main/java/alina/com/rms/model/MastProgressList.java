package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MastProgressList {

@SerializedName("project_name")
@Expose
private String projectName;
@SerializedName("group_name")
@Expose
private String groupName;
@SerializedName("section_name")
@Expose
private String sectionName;
@SerializedName("mast_prog_no")
@Expose
private String mastProgNo;
@SerializedName("mast_date")
@Expose
private String mastDate;
@SerializedName("mast_prog_id")
@Expose
private String mastProgId;

public String getProjectName() {
return projectName;
}

public void setProjectName(String projectName) {
this.projectName = projectName;
}

public String getGroupName() {
return groupName;
}

public void setGroupName(String groupName) {
this.groupName = groupName;
}

public String getSectionName() {
return sectionName;
}

public void setSectionName(String sectionName) {
this.sectionName = sectionName;
}

public String getMastProgNo() {
return mastProgNo;
}

public void setMastProgNo(String mastProgNo) {
this.mastProgNo = mastProgNo;
}

public String getMastDate() {
return mastDate;
}

public void setMastDate(String mastDate) {
this.mastDate = mastDate;
}

public String getMastProgId() {
return mastProgId;
}

public void setMastProgId(String mastProgId) {
this.mastProgId = mastProgId;
}

}