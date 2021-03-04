package ca.unb.mobiledev.pm_app.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Projects {

    private String projectName;
    private String id;
    private HashMap<String, String> members;


    public Projects(){
    }

    public Projects(String projectName, String id, HashMap<String, String> members) {
        this.projectName = projectName;
        this.id = id;
        this.members = members;
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
