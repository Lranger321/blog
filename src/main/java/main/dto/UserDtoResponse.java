package main.dto;

public class UserDtoResponse {

    private int id;

    private String name;

    private String photo;

    private String email;

    private Boolean moderation;

    private int moderationCount;

    private Boolean settings;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getModeration() {
        return moderation;
    }

    public void setModeration(Boolean moderation) {
        this.moderation = moderation;
    }

    public int getModerationCount() {
        return moderationCount;
    }

    public void setModerationCount(int moderationCount) {
        this.moderationCount = moderationCount;
    }

    public Boolean getSettings() {
        return settings;
    }

    public void setSettings(Boolean settings) {
        this.settings = settings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
