package main.persistence.service;

import main.dto.response.CalendarInfo;
import main.dto.response.PostDtoResponse;
import main.dto.response.PostViewResponse;
import main.dto.response.PostsInfo;
import main.persistence.entity.ModerationStatus;
import main.persistence.entity.Post;
import main.persistence.repository.PostPageRepository;
import main.persistence.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PostGettingService(PostRepository postRepository, PostPageRepository postPageRepository) {
        this.postRepository = postRepository;
        this.postPageRepository = postPageRepository;
    }

    public PostsInfo getPostsForModeration(int offset, int limit, String status, String userName) {
        List<Post> posts = new ArrayList<>();
        long count = 0;
        Pageable pageable = PageRequest.of(offset,offset+limit);
        switch (status) {
            case "new":
                posts = postPageRepository.findAllNewForModeration(pageable);
                count = postRepository.countOfModeration();
                break;
            case "declined":
                posts = postPageRepository.findAllForModeration(pageable,ModerationStatus.DECLINED,userName);
                count = postRepository.countOfPostByUserAndModerationStatus(ModerationStatus.DECLINED, userName);
                break;
            case "accepted":
                posts = postPageRepository.findAllForModeration(pageable,ModerationStatus.ACCEPTED,userName);
                count = postRepository.countOfPostByUserAndModerationStatus(ModerationStatus.DECLINED, userName);
                break;
        }
        List<PostDtoResponse> postDtoResponseList = Converter.createPostDtoList(posts);
        return Converter.convertToPostsInfo(postDtoResponseList, count);
    }

    public PostsInfo getPostByUser(int offset, int limit, String status, String name) {
        List<Post> posts = new ArrayList<>();
        long count = 0;
        Pageable pageable = PageRequest.of(offset,offset+limit);
        switch (status) {
            case "inactive":
                posts = postPageRepository.findInactivePostByUserId(pageable, name);
                break;
            case "pending":
                posts = postPageRepository.findPostsByUserId(pageable, name, ModerationStatus.NEW);
                break;
            case "declined":
                posts = postPageRepository.findPostsByUserId(pageable, name, ModerationStatus.DECLINED);
                break;
            case "published":
                posts = postPageRepository.findPostsByUserId(pageable, name, ModerationStatus.ACCEPTED);
                break;
        }
        List<PostDtoResponse> list = Converter.createPostDtoList(posts);
        return Converter.convertToPostsInfo(list, count);
    }

    public PostsInfo searchPosts(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset,offset+limit);
        List<PostDtoResponse> posts = Converter.createPostDtoList(postPageRepository.searchByText(pageable,query));
        long count = postRepository.countByText(query);
        return Converter.convertToPostsInfo(posts, count);
    }

    public PostsInfo getPosts(int offset, int limit, String mode) {
        List<Post> posts = new ArrayList<>();
        Pageable pageable;
        switch (mode) {
            //by  сортировать по дате публикации, выводить сначала новые (если
            // mode не задан, использовать это значение по умолчанию
            case "recent":
                pageable = PageRequest.of(offset, limit, Sort.by("time").descending());
                posts.addAll((postPageRepository.findAllByIsActiveAndModerationStatus
                        (true, ModerationStatus.ACCEPTED, pageable)));
                break;
            // сортировать по убыванию количества комментариев (посты без комментариев выводить)
            case "popular":
                pageable = PageRequest.of(offset, limit);
                posts.addAll((postPageRepository.sortedByComments(pageable)));
                break;
            //- сортировать по убыванию количества лайков (посты без лайков дизлайков выводить)
            // Todo соритирует по кол-во лайков и дизлайков
            case "best":
                pageable = PageRequest.of(offset, limit);
                posts.addAll(postPageRepository.sortedByLikes(pageable));
                break;
            //сортировать по дате публикации, выводить сначала старые
            case "early":
                pageable = PageRequest.of(offset, limit, Sort.by("time").ascending());
                posts.addAll((postPageRepository.findAllByIsActiveAndModerationStatus
                        (true, ModerationStatus.ACCEPTED, pageable)));
                break;
        }
        long count = postRepository.count();
        return Converter.convertToPostsInfo(Converter.createPostDtoList(posts), count);
    }

    public PostsInfo getPostByDate(int offset, int limit, String date) {
        List<Post> posts = new ArrayList<>();
        Pageable pageable = PageRequest.of(offset,offset+limit);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(date));
            calendar.add(Calendar.DATE,1);
            posts = postPageRepository.findPostByDate(pageable,dateFormat.parse(date),calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Converter.convertToPostsInfo(Converter.createPostDtoList(posts), posts.size());
    }

    public PostsInfo getPostByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset,offset+limit);
        List<PostDtoResponse> posts = Converter.createPostDtoList(postPageRepository.findPostsByTag(pageable, tag));
        return Converter.convertToPostsInfo(posts, posts.size());
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

    public CalendarInfo getCalendar(String inputYear) {
        if (inputYear == null) {
            inputYear = dateFormat.format(new Date()).split("-")[0];
        }
        List<Integer> years = new ArrayList<>();
        HashMap<String, Integer> postsCalendar = new HashMap<>();
        String finalInputYear = inputYear;
        postRepository.findAll().forEach(post -> {
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

}
