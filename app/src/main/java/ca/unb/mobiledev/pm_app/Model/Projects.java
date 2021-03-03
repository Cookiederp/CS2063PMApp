package ca.unb.mobiledev.pm_app.Model;

import java.util.ArrayList;

public class Projects {

    private String projectName;
    private String id;


    public Projects(){
    }

    public Projects(String projectName, String id) {
        this.projectName = projectName;
        this.id = id;
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
}
