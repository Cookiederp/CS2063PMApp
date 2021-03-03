package ca.unb.mobiledev.pm_app.Model;

import java.util.ArrayList;

public class Users {

    private String id;
    private String firstName;
    private String lastName;
    private String profilePicURL;
    //projects user owns or joined and is currently still in
    private ArrayList<String> projectIds;

    public Users(){}

    public Users(String id, String firstName, String lastName, String profilePicURL, ArrayList<String> projectIds){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicURL = profilePicURL;
        this.projectIds = projectIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public ArrayList<String> getProjectIds() {
        return projectIds;
    }

    public void addProjectId(String projectId) {
        this.projectIds.add(projectId);
    }
}
