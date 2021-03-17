package main.persistence.service;

import com.google.gson.Gson;
import main.dto.response.*;
import main.persistence.entity.Comment;
import main.persistence.entity.Post;
import main.persistence.entity.User;
import main.persistence.entity.Vote;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Converter {

    public static List<PostDtoResponse> createPostDtoList(List<Post> posts) {
        List<PostDtoResponse> responseList = new ArrayList<>();

        posts.forEach(post -> {
            UserDtoResponse user = new UserDtoResponse();
            user.setId(post.getUser().getId());
            user.setName(post.getUser().getName());
            int likeCount = (int) post.getVotes().stream().filter(vote -> vote.getValue()==1).count();
            int disLikeCount = (int) post.getVotes().stream().filter(vote -> vote.getValue()==-1).count();
            PostDtoResponse postInfo = new PostDtoResponse(post.getId(),
                    post.getTime().getTime() / 1000, user,
                    post.getTitle(), Jsoup.parse(post.getText()).text(),
                    likeCount, disLikeCount,
                    post.getComments().size(), post.getViewCount());
            responseList.add(postInfo);
        });
        return responseList;
    }

    public static PostsInfo convertToPostsInfo(List<PostDtoResponse> posts, long count) {
        PostsInfo postsInfo = new PostsInfo();
        postsInfo.setCount(count);
        postsInfo.setPosts(posts);
        return postsInfo;
    }

    public static AuthResponse createAuthResponse(Boolean status) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(status);
        return authResponse;
    }

    public static AuthResponse createAuthResponse(Boolean status, User user, long moderationCount) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(status);
        UserDtoResponse userDtoResponse = new UserDtoResponse();
        userDtoResponse.setId(user.getId());
        userDtoResponse.setName(user.getName());
        userDtoResponse.setModerationCount(moderationCount);
        userDtoResponse.setEmail(user.getEmail());
        userDtoResponse.setModeration(user.isModerator());
        userDtoResponse.setId(user.getId());
        userDtoResponse.setSettings(true);
        authResponse.setUser(userDtoResponse);
        System.out.println(new Gson().toJson(authResponse,AuthResponse.class));
        return authResponse;
    }

    public static PostViewResponse createPostViewResponse(Post post) {
        UserDtoResponse userDtoResponse = new UserDtoResponse();
        userDtoResponse.setName(post.getUser().getName());
        userDtoResponse.setId(post.getUser().getId());
        int likeCount = (int) post.getVotes().stream().filter(vote -> vote.getValue()==1).count();
        int disLikeCount = (int) post.getVotes().stream().filter(vote -> vote.getValue()==-1).count();
        List<String> tags = new ArrayList<>();
        post.getTagList().forEach(tag -> tags.add(tag.getName()));
        PostViewResponse postViewResponse = new PostViewResponse(
                post.getId(),
                post.getTime().getTime() / 1000,
                userDtoResponse,
                post.getTitle(),
                Jsoup.parse(post.getText()).text(),
                likeCount,
                disLikeCount,
                post.getViewCount(),
                commentInfos(post.getComments()),
                tags
        );
        return postViewResponse;
    }

    public static CalendarInfo createCalendarInfo(HashMap<String, Integer> postsCalendar,List<Integer> years){
        CalendarInfo calendarInfo = new CalendarInfo();
        calendarInfo.setPosts(postsCalendar);
        calendarInfo.setYears(years);
        return calendarInfo;
    }

    private static List<CommentInfo> commentInfos(List<Comment> comments) {
        List<CommentInfo> commentInfos = new ArrayList<>();
        comments.forEach(comment -> {
            UserDtoResponse user = new UserDtoResponse();
            user.setName(comment.getUser().getName());
            user.setId(comment.getUser().getId());
            user.setPhoto(comment.getUser().getPhoto());
           CommentInfo commentInfo = new CommentInfo(comment.getId(),
                    comment.getTime().getTime() / 1000,
                    comment.getText(), user);
            commentInfos.add(commentInfo);
           commentInfos.add(commentInfo);
        });
        return commentInfos;
    }



}
