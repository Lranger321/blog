package main.persistence.service;

import main.dto.CalendarInfo;
import main.dto.PostDtoResponse;
import main.dto.PostsInfo;
import main.persistence.PostDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    PostDAO postDAO;


    public PostsInfo getPosts(int offset, int limit, String mode) {
        List<PostDtoResponse> posts = postDAO.getPosts(mode);
        return getPostInfo(posts, offset, limit);
    }

    public PostsInfo getPostByDate(int offset, int limit, String date) {
        List<PostDtoResponse> posts = postDAO.getPostByDate(date);
        return getPostInfo(posts, offset, limit);
    }

    public PostsInfo getPostByTag(int offset, int limit, String tag) {
        List<PostDtoResponse> posts = postDAO.getPostByTag(tag);
        return getPostInfo(posts, offset, limit);
    }

    private PostsInfo getPostInfo(List<PostDtoResponse> posts, int offset, int limit) {
        PostsInfo postsInfo = new PostsInfo();
        int count = posts.size();
        postsInfo.setCount(count);
        if (offset + limit < count) {
            postsInfo.setPosts(posts.subList(offset, offset + limit));
        } else {
            postsInfo.setPosts(posts.subList(offset, count));
        }
        postsInfo.setPosts(posts);
        return postsInfo;
    }

    public CalendarInfo getCalendar(String inputYear){
        return postDAO.calendarInfo(inputYear);
    }

}
