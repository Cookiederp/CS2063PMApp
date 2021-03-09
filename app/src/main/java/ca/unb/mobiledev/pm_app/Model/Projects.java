package ca.unb.mobiledev.pm_app.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Projects {

    private String projectName;
    private String id;
    private HashMap<String, String> members;
    private String iconPicURL;


    public Projects(){
    }

    public Projects(String projectName, String id, HashMap<String, String> members, String iconPicURL) {
        this.projectName = projectName;
        this.id = id;
        this.members = members;
        this.iconPicURL = iconPicURL;
    }

    public String getIconPicURL() {
        return iconPicURL;
    }

    public void setIconPicURL(String iconPicURL) {
        this.iconPicURL = iconPicURL;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, String> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, String> members) {
        this.members = members;
    }
}
