package alina.com.rms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectList {

@SerializedName("project_id")
@Expose
private String projectId;
@SerializedName("proj_name")
@Expose
private String projName;

public String getProjectId() {
return projectId;
}

public void setProjectId(String projectId) {
this.projectId = projectId;
}

public String getProjName() {
return projName;
}

public void setProjName(String projName) {
this.projName = projName;
}

}