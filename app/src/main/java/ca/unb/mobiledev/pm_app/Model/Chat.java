package ca.unb.mobiledev.pm_app.Model;

public class Chat {

    private String sender;
    private String projectId;
    private String message;
    private Long timestamp;
    private String imageURL;
    private String senderName;

    public Chat() {
    }

    public Chat(String sender, String projectId, String message, Long timestamp, String imageURL, String senderName) {
        this.sender = sender;
        this.projectId = projectId;
        this.message = message;
        this.timestamp = timestamp;
        this.imageURL = imageURL;
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
