package main.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

public class PostViewResponse {

    private int id;
    private long timestamp;
    private UserDtoResponse user;
    private String title;
    private String text;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private List<CommentInfo> comments;
    private List<String> tags;

    public PostViewResponse(int id, long timestamp,
                            UserDtoResponse user, String title,
                            String text, int likeCount, int dislikeCount,
                            int viewCount, List<CommentInfo> comments,
                            List<String> tags) {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user;
        this.title = title;
        this.text = text;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.viewCount = viewCount;
        this.comments = comments;
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public UserDtoResponse getUser() {
        return user;
    }

    public void setUser(UserDtoResponse user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public List<CommentInfo> getComments() {
        return comments;
    }

    public void setComments(List<CommentInfo> comments) {
        this.comments = comments;
    }
}
