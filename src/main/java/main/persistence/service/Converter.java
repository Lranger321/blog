package main.persistence.service;

import main.dto.*;
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
            int likeCount = (int) post.getVotes().stream().filter(Vote::getValue).count();
            int disLikeCount = (int) post.getVotes().stream().filter(vote -> !vote.getValue()).count();
            PostDtoResponse postInfo = new PostDtoResponse(post.getId(),
                    post.getTime().getTime() / 1000, user,
                    post.getTitle(), post.getText(),
                    likeCount, disLikeCount,
                    post.getComments().size(), post.getViewCount());
            responseList.add(postInfo);
        });
        return responseList;
    }

    public static AuthResponse createAuthResponse(Boolean status) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(status);
        return authResponse;
    }

    public static AuthResponse createAuthResponse(Boolean status, User user) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(true);
        return authResponse;
    }

    public static PostViewResponse createPostViewResponse(Post post) {
        UserDtoResponse userDtoResponse = new UserDtoResponse();
        int likeCount = (int) post.getVotes().stream().filter(Vote::getValue).count();
        int disLikeCount = (int) post.getVotes().stream().filter(vote -> !vote.getValue()).count();
        PostViewResponse postViewResponse = new PostViewResponse(
                post.getId(),
                post.getTime().getTime() / 1000,
                userDtoResponse,
                post.getTitle(),
                Jsoup.parse(post.getText()).text(),
                likeCount,
                disLikeCount,
                post.getViewCount(),
                commentInfos(post.getComments())
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
        });
        return commentInfos;
    }




}
