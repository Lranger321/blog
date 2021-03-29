package main.persistence.service;

import main.dto.response.CalendarInfo;
import main.dto.response.PostViewResponse;
import main.dto.response.PostsInfo;
import main.persistence.entity.ModerationStatus;
import main.persistence.entity.Post;
import main.persistence.entity.PostCalendar;
import main.persistence.repository.CalendarRepository;
import main.persistence.repository.PostPageRepository;
import main.persistence.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostGettingService {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final PostRepository postRepository;

    private final PostPageRepository postPageRepository;

    private final CalendarRepository calendarRepository;

    @Autowired
    public PostGettingService(PostRepository postRepository, PostPageRepository postPageRepository,
                              CalendarRepository calendarRepository) {
        this.postRepository = postRepository;
        this.postPageRepository = postPageRepository;
        this.calendarRepository = calendarRepository;
    }

    public PostsInfo getPostsForModeration(int offset, int limit, String status, String userName) {
        Page<Post> pagePosts = null;
        Pageable pageable = PageRequest.of(offset, offset + limit);
        switch (status) {
            case "new":
                pagePosts = postPageRepository.findAllNewForModeration(pageable);
                break;
            case "declined":
                pagePosts = postPageRepository.findAllForModeration(pageable, ModerationStatus.DECLINED, userName);
                break;
            case "accepted":
                pagePosts = postPageRepository.findAllForModeration(pageable, ModerationStatus.ACCEPTED, userName);
                break;
        }
        if (pagePosts != null) {
            long count = pagePosts.getTotalElements();
            List<Post> posts = pagePosts.getContent();
            return Converter.convertToPostsInfo(
                    Converter.createPostDtoList(posts), count);
        }
        return new PostsInfo();
    }

    public PostsInfo getPostByUser(int offset, int limit, String status, String name) {
        Page<Post> pagePosts = null;
        Pageable pageable = PageRequest.of(offset, offset + limit);
        switch (status) {
            case "inactive":
                pagePosts = postPageRepository.findInactivePostByUserId(pageable, name);
                break;
            case "pending":
                pagePosts = postPageRepository.findPostsByUserId(pageable, name, ModerationStatus.NEW);
                break;
            case "declined":
                pagePosts = postPageRepository.findPostsByUserId(pageable, name, ModerationStatus.DECLINED);
                break;
            case "published":
                pagePosts = postPageRepository.findPostsByUserId(pageable, name, ModerationStatus.ACCEPTED);
                break;
        }
        if (pagePosts != null) {
            List<Post> posts = pagePosts.getContent();
            long count = pagePosts.getTotalElements();
            return Converter.convertToPostsInfo(
                    Converter.createPostDtoList(posts), count);
        }
        return new PostsInfo();
    }

    public PostsInfo searchPosts(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset, offset + limit);
        Page<Post> pagePosts = postPageRepository.searchByText(pageable, query);
        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);
        long count = pagePosts.getTotalElements();
        return Converter.convertToPostsInfo(
                Converter.createPostDtoList(posts), count);
    }

    public PostsInfo getPosts(int offset, int limit, String mode) {
        Page<Post> pagePosts = null;
        Pageable pageable;
        switch (mode) {
            //by  сортировать по дате публикации, выводить сначала новые (если
            // mode не задан, использовать это значение по умолчанию
            case "recent":
                pageable = PageRequest.of(offset, limit, Sort.by("time").descending());
                pagePosts = (postPageRepository.findAllByIsActiveAndModerationStatus
                        (true, ModerationStatus.ACCEPTED, pageable));
                break;
            // сортировать по убыванию количества комментариев (посты без комментариев выводить)
            case "popular":
                pageable = PageRequest.of(offset, limit);
                pagePosts = (postPageRepository.sortedByComments(pageable));
                break;
            //- сортировать по убыванию количества лайков (посты без лайков дизлайков выводить)
            case "best":
                pageable = PageRequest.of(offset, limit);
                pagePosts = (postPageRepository.sortedByLikes(pageable));
                break;
            //сортировать по дате публикации, выводить сначала старые
            case "early":
                pageable = PageRequest.of(offset, limit, Sort.by("time").ascending());
                pagePosts = (postPageRepository.findAllByIsActiveAndModerationStatus
                        (true, ModerationStatus.ACCEPTED, pageable));
                break;
        }
        if (pagePosts != null) {
            long count = pagePosts.getTotalElements();
            return Converter.convertToPostsInfo(
                    Converter.createPostDtoList(pagePosts.getContent()), count);
        }
        return new PostsInfo();
    }

    public PostsInfo getPostByDate(int offset, int limit, String date) {
        Page<Post> pagePosts;
        Pageable pageable = PageRequest.of(offset, offset + limit);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(date));
            calendar.add(Calendar.DATE, 1);
            pagePosts = postPageRepository.findPostByDate(pageable, dateFormat.parse(date), calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        long count = pagePosts.getTotalElements();
        return Converter.convertToPostsInfo(
                Converter.createPostDtoList(pagePosts.getContent()), count);
    }

    public PostsInfo getPostByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset, offset + limit);
        Page<Post> pagePosts = postPageRepository.findPostsByTag(pageable, tag);
        long count = pagePosts.getTotalElements();
        return Converter.convertToPostsInfo(
                Converter.createPostDtoList(pagePosts.getContent()), count);
    }

    public PostViewResponse getPostById(int id) {
        PostViewResponse postViewResponse;
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            postViewResponse = null;
        } else {
            postViewResponse = Converter.createPostViewResponse(post);
        }
        return postViewResponse;
    }

    public CalendarInfo getCalendar(Integer inputYear) {
        if (inputYear == null) {
            inputYear = Integer.valueOf(dateFormat.format(new Date()).split("-")[0]);
        }
        String fromDate = inputYear+"-01-01";
        String toDate = inputYear+"-12-31";
        List<Integer> years = calendarRepository.getYears();
        List<PostCalendar> postsCalendar = null;
        try {
            postsCalendar = calendarRepository.getCalendar(dateFormat.parse(fromDate),dateFormat.parse(toDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return Converter.createCalendarInfo(postsCalendar, years);
    }

}
