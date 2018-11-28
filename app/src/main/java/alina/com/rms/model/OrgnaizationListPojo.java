package alina.com.rms.model;

/**
 * Created by alina on 17-03-2018.
 */

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class OrgnaizationListPojo {

    @SerializedName("org_id")
    @Expose
    private String orgId;
    @SerializedName("org_name")
    @Expose
    private String orgName;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

}