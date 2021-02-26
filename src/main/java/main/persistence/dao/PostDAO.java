package main.persistence.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import main.persistence.entity.ModerationStatus;
import main.persistence.entity.Post;
import main.persistence.entity.Tag;
import main.persistence.repository.PostPageRepository;
import main.persistence.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostDAO {

    private final PostRepository postRepository;

    private final PostPageRepository postPageRepository;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public PostDAO(PostRepository postRepository, PostPageRepository postPageRepository) {
        this.postRepository = postRepository;
        this.postPageRepository = postPageRepository;
    }

    public List<Post> getPosts(String mode, int offset, int limit) {
        return (mode != null) ? getSortedPosts(mode, offset, limit) :
                getSortedPosts("recent", offset, limit);
    }

    private List<Post> getSortedPosts(String mode, int offset, int limit) {
        Pageable pageable = null;
        List<Post> posts = new ArrayList<>();
        limit += offset;
        System.out.println(mode + " " + offset + " " + limit);
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
            // Todo не работает
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
        //  System.out.println("posts: " + new Gson().toJson(posts, List.class));
        return posts;
    }

    public List<Post> getPostByDate(String date) {
        List<Post> posts = getSortedPosts("recent", 0, getPostCount());
        posts.removeIf(post -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(post.getTime().getTime());
            String postDate = dateFormat.format(calendar.getTime());
            return !(postDate.equals(date));
        });
        return posts;
    }

    public List<Post> getPostByTag(String tag) {
        List<Post> list = postRepository.findAll();
        return list.stream().filter(post -> {
            boolean containsTag = false;
            for (Tag postTag : post.getTagList()) {
                if (postTag.getName().equals(tag)) {
                    containsTag = true;
                    break;
                }
            }
            return containsTag;
        }).collect(Collectors.toList());
    }

    public List<Post> searchPosts(int offset, int limit, String text) {
        Pageable pageable = PageRequest.of(offset, limit + offset);
        return postPageRepository.searchByText(pageable, text);
    }

    public int getCountByQuery(String text) {
        return (int) postRepository.countByText(text);
    }

    public Post getPostById(int id) {
        return postRepository.findById(id);
    }

    public int getPostCount() {
        return (int) postRepository.count();
    }

    public List<Post> getPostByUserId(int offset,int limit,String email){
        Pageable pageable = PageRequest.of(offset,offset+limit);
        return postPageRepository.findByUserId(pageable,email);
    }

    public List<Post> getNewPostsForModeration(int offset,int limit){
        Pageable pageable = PageRequest.of(offset,offset+limit);
        return postPageRepository.findAllNewForModeration(pageable);
    }

    public List<Post> getPostForModeration(int offset,int limit,ModerationStatus moderationStatus,String email){
        Pageable pageable = PageRequest.of(offset,limit);
        return postPageRepository.findAllForModeration(pageable,moderationStatus,email);
    }



}


