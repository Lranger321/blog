package main.dto;

import java.util.List;

public class PostsInfo {

    private int count;

    private List<PostDtoResponse> posts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostDtoResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDtoResponse> posts) {
        this.posts = posts;
    }
}
