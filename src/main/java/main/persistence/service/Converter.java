package main.persistence.service;

import main.dto.response.*;
import main.persistence.entity.Comment;
import main.persistence.entity.Post;
import main.persistence.entity.PostCalendar;
import main.persistence.entity.User;
import org.jsoup.Jsoup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Converter {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static List<PostDtoResponse> createPostDtoList(List<Post> posts) {
        List<PostDtoResponse> responseList = new ArrayList<>();
        posts.forEach(post -> {
            UserDtoResponse user = new UserDtoResponse();
            user.setId(post.getUser().getId());
            user.setName(post.getUser().getName());
            int likeCount = (int) post.getVotes().stream().filter(vote -> vote.getValue() == 1).count();
            int disLikeCount = (int) post.getVotes().stream().filter(vote -> vote.getValue() == -1).count();
            PostDtoResponse postInfo = PostDtoResponse.builder()
                    .id(post.getId())
                    .timestamp(post.getTime().getTime() / 1000)
                    .user(user)
                    .title(post.getTitle())
                    .announce(Jsoup.parse(post.getText()).text())
                    .likeCount(likeCount)
                    .dislikeCount(disLikeCount)
                    .commentCount(post.getComments().size())
                    .viewCount(post.getViewCount())
                    .build();
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
        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .moderationCount(moderationCount)
                .email(user.getEmail())
                .photo(user.getPhoto())
                .moderation(user.isModerator())
                .settings(true)
                .build();
        authResponse.setUser(userDtoResponse);
        return authResponse;
    }

    public static PostViewResponse createPostViewResponse(Post post) {
        UserDtoResponse userDtoResponse = new UserDtoResponse();
        userDtoResponse.setName(post.getUser().getName());
        userDtoResponse.setId(post.getUser().getId());
        int likeCount = (int) post.getVotes().stream().filter(vote -> vote.getValue() == 1).count();
        int disLikeCount = (int) post.getVotes().stream().filter(vote -> vote.getValue() == -1).count();
        List<String> tags = new ArrayList<>();
        post.getTagList().forEach(tag -> tags.add(tag.getName()));
        PostViewResponse postViewResponse = PostViewResponse.builder()
                .id(post.getId())
                .timestamp(post.getTime().getTime() / 1000)
                .user(userDtoResponse)
                .title(post.getTitle())
                .text(post.getText())
                .likeCount(likeCount)
                .dislikeCount(disLikeCount)
                .viewCount(post.getViewCount())
                .comments(commentInfos(post.getComments()))
                .tags(tags)
                .build();
        return postViewResponse;
    }

    public static CalendarInfo createCalendarInfo(List<PostCalendar> postsCalendar, List<Integer> years) {
        CalendarInfo calendarInfo = new CalendarInfo();
        HashMap<String, Long> posts = new HashMap<>();
        for (PostCalendar postCalendar : postsCalendar) {
            posts.put(dateFormat.format(postCalendar.getDate()), postCalendar.getCount());
        }
        calendarInfo.setPosts(posts);
        calendarInfo.setYears(years);
        return calendarInfo;
    }

    private static List<CommentInfo> commentInfos(List<Comment> comments) {
        List<CommentInfo> commentInfos = new ArrayList<>();
        comments.forEach(comment -> {
            UserDtoResponse user = UserDtoResponse.builder()
                    .name(comment.getUser().getName())
                    .id(comment.getUser().getId())
                    .photo(comment.getUser().getPhoto())
                    .build();
            CommentInfo commentInfo = CommentInfo.builder()
                    .id(comment.getId())
                    .timestamp(comment.getTime().getTime() / 1000)
                    .text(comment.getText())
                    .user(user)
                    .build();
            commentInfos.add(commentInfo);
        });
        return commentInfos;
    }


}
