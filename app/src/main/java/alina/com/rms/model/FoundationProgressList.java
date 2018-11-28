package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoundationProgressList implements Serializable{

@SerializedName("project_name")
@Expose
private String projectName;
@SerializedName("group_name")
@Expose
private String groupName;
@SerializedName("section_name")
@Expose
private String sectionName;
@SerializedName("foundation_prog_no")
@Expose
private String foundationProgNo;
@SerializedName("foundation_date")
@Expose
private String foundationDate;
@SerializedName("foundation_prog1__id")
@Expose
private String foundationProg1Id;

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

public String getFoundationProgNo() {
return foundationProgNo;
}

public void setFoundationProgNo(String foundationProgNo) {
this.foundationProgNo = foundationProgNo;
}

public String getFoundationDate() {
return foundationDate;
}

public void setFoundationDate(String foundationDate) {
this.foundationDate = foundationDate;
}

public String getFoundationProg1Id() {
return foundationProg1Id;
}

public void setFoundationProg1Id(String foundationProg1Id) {
this.foundationProg1Id = foundationProg1Id;
}

}