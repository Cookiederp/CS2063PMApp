package ca.unb.mobiledev.pm_app.Model;

public class Tasks {

    private String id;
    private String projectId;
    private String title;
    private String description;
    private String deadline;


    public Tasks(){
    }

    public Tasks(String id, String projectId, String title, String description, String deadline) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
