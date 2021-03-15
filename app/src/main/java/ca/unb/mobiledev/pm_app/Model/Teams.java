package ca.unb.mobiledev.pm_app.Model;

import java.util.ArrayList;

public class Teams {
    String teamName;
    String teamID;

    public Teams(){

    }
    public Teams(String teamName, String teamID) {
        this.teamName = teamName;
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getId() {
        return teamID;
    }

    public void setId(String teamID) {
        this.teamID = teamID;
    }
}
