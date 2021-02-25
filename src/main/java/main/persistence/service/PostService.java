package main.persistence.service;

import com.google.gson.Gson;
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
    private PostDAO postDAO;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public PostsInfo getPosts(int offset, int limit, String mode) {
        List<PostDtoResponse> posts = Converter.createPostDtoList(postDAO.getPosts(mode,offset,limit));
        int count = postDAO.getPostCount();
        return getPostInfo(posts, count);
    }

    public PostsInfo getPostByDate(int offset, int limit, String date) {
        List<PostDtoResponse> posts = Converter.createPostDtoList(postDAO.getPostByDate(date));
        return getPostInfo(posts, posts.size());
    }

    public PostsInfo getPostByTag(int offset, int limit, String tag) {
        List<PostDtoResponse> posts = Converter.createPostDtoList(postDAO.getPostByTag(tag));
        return getPostInfo(posts,posts.size());
    }

    private PostsInfo getPostInfo(List<PostDtoResponse> posts, int count) {
        PostsInfo postsInfo = new PostsInfo();
        postsInfo.setCount(count);
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
        postDAO.getPosts("recent",0,postDAO.getPostCount()).forEach(post -> {
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
        List<PostDtoResponse> posts = Converter.createPostDtoList(postDAO.searchPosts(offset, limit, query));
        int count =postDAO.getCountByQuery(query);
        return getPostInfo(posts,count);
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
