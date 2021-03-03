package main.persistence.service;

import main.dto.responce.*;
import main.dto.responce.PostsInfo;
import main.persistence.dao.PostDAO;
import main.persistence.dao.UserDAO;
import main.persistence.entity.ModerationStatus;
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

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final PostDAO postDAO;

    @Autowired
    public PostService(PostDAO postDAO) {
        this.postDAO = postDAO;
    }


    public PostsInfo getPosts(int offset, int limit, String mode) {
        List<PostDtoResponse> posts = Converter.createPostDtoList(postDAO.getPosts(mode,offset,limit));
        int count = postDAO.getPostCount();
        return Converter.convertToPostsInfo(posts, count);
    }

    public PostsInfo getPostByDate(int offset, int limit, String date) {
        List<PostDtoResponse> posts = Converter.createPostDtoList(postDAO.getPostByDate(date));
        return Converter.convertToPostsInfo(posts, posts.size());
    }

    public PostsInfo getPostByTag(int offset, int limit, String tag) {
        List<PostDtoResponse> posts = Converter.createPostDtoList(postDAO.getPostByTag(tag));
        return Converter.convertToPostsInfo(posts,posts.size());
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
        return Converter.convertToPostsInfo(posts,count);
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

    public PostsInfo getPostsForModeration(int offset,int limit,String status,String userName){
        List<Post> posts = new ArrayList<>();
        int count = 0;
            switch (status) {
                case "new":
                    posts = postDAO.getNewPostsForModeration(offset, limit);
                    count = postDAO.getCountForModeration();
                    break;
                case "declined":
                    posts = postDAO.getPostForModeration(offset,limit, ModerationStatus.DECLINED,userName);
                    count = postDAO.getCountForModerationByStatusAndUser(ModerationStatus.DECLINED,userName);
                    break;
                case "accepted":
                    posts = postDAO.getPostForModeration(offset,limit,ModerationStatus.ACCEPTED,userName);
                    count = postDAO.getCountForModerationByStatusAndUser(ModerationStatus.ACCEPTED,userName);
                    break;
            }
            List<PostDtoResponse> postDtoResponseList = Converter.createPostDtoList(posts);
        return Converter.convertToPostsInfo(postDtoResponseList,count);
    }



    public PostsInfo getPostByUser(int offset, int limit, String status, String name) {
        List<Post> posts = new ArrayList<>();
        int count = 0;
        switch (status){
            case "inactive":
                posts = postDAO.getInactivePostByUser(offset,limit,name);
                break;
            case "pending":
                posts = postDAO.getPostByUserAndModerationStatus(offset,limit,name,ModerationStatus.NEW);
                break;
            case "declined":
                posts = postDAO.getPostByUserAndModerationStatus(offset,limit,name,ModerationStatus.DECLINED);
                break;
            case "published":
                posts = postDAO.getPostByUserAndModerationStatus(offset,limit,name,ModerationStatus.ACCEPTED);
                break;
        }
        List<PostDtoResponse> list = Converter.createPostDtoList(posts);
        return Converter.convertToPostsInfo(list,count);
    }

}
