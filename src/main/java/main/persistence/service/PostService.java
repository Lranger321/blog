package main.persistence.service;

import main.dto.CalendarInfo;
import main.dto.PostDtoResponse;
import main.dto.PostViewResponse;
import main.dto.PostsInfo;
import main.persistence.dao.PostDAO;
import main.persistence.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostDAO postDAO;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public PostsInfo getPosts(int offset, int limit, String mode) {
        List<PostDtoResponse> posts = Converter.createPostDtoList(postDAO.getPosts(mode));
        return getPostInfo(posts, offset, limit);
    }

    public PostsInfo getPostByDate(int offset, int limit, String date) {
        List<PostDtoResponse> posts = Converter.createPostDtoList(postDAO.getPostByDate(date));
        return getPostInfo(posts, offset, limit);
    }

    public PostsInfo getPostByTag(int offset, int limit, String tag) {
        List<PostDtoResponse> posts = Converter.createPostDtoList(postDAO.getPostByTag(tag));
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

    public CalendarInfo getCalendar(String inputYear) {
        if (inputYear == null) {
            inputYear = dateFormat.format(new Date()).split("-")[0];
        }
        List<Integer> years = new ArrayList<>();
        HashMap<String, Integer> postsCalendar = new HashMap<>();
        String finalInputYear = inputYear;
        postDAO.getPosts("recent").forEach(post -> {
            String date = dateFormat.format(post.getTime());
            int year = Integer.parseInt(date.split("-")[0]);
            if (!years.contains(year)) {
                years.add(year);
            }
            if (Integer.toString(year).equals(finalInputYear)) {
                postsCalendar.put(date, postsCalendar.getOrDefault(date, 0) + 1);
            }
        });
        return Converter.createCalendarInfo(postsCalendar, years);
    }

    public PostsInfo searchPosts(int offset, int limit, String query) {
        return new PostsInfo();
    }

    public PostViewResponse getPostById(int id) {
        PostViewResponse postViewResponse;
        Post post = postDAO.getPostById(id);
        if (post == null) {
            postViewResponse = null;
        } else {
            postViewResponse = Converter.createPostViewResponse(post);
        }
        return postViewResponse;
    }


}
