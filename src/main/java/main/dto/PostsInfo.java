package main.dto;

import java.util.ArrayList;
import java.util.List;

public class PostsInfo {

    private int count = 0;

    private List<PostDtoResponse> posts = new ArrayList<>();

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
