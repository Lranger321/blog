package main.dto;

public class CommentInfo {

    private int id;

    private long timestamp;

    private String text;

    private UserDtoResponse user;

    public CommentInfo(int id, long timestamp, String text, UserDtoResponse user) {
        this.id = id;
        this.timestamp = timestamp;
        this.text = text;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDtoResponse getUser() {
        return user;
    }

    public void setUser(UserDtoResponse user) {
        this.user = user;
    }
}
