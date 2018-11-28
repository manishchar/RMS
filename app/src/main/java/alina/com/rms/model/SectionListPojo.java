package alina.com.rms.model;

/**
 * Created by alina on 17-03-2018.
 */

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class SectionListPojo {

    @SerializedName("section_name")
    @Expose
    private String sectionName;

    @SerializedName("section_id")
    @Expose
    private String section_id;
    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }
}