package main.persistence.dao;

import java.text.DateFormat;
import java.text.ParseException;
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

    public void savePost(Post post){
        postRepository.save(post);
    }

    public List<Post> getPosts(String mode, int offset, int limit) {
        return (mode != null) ? getSortedPosts(mode, offset, limit) :
                getSortedPosts("recent", offset, limit);
    }

    private List<Post> getSortedPosts(String mode, int offset, int limit) {
        Pageable pageable = null;
        List<Post> posts = new ArrayList<>();
        limit += offset;
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
        return posts;
    }

    public List<Post> getPostByDate(int offset,int limit,String date) {
        Pageable pageable = PageRequest.of(offset,offset+limit);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(date));
            calendar.add(Calendar.DATE,1);
            return postPageRepository.findPostByDate(pageable,dateFormat.parse(date),calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Post> getPostByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset,offset+limit);
        return postPageRepository.findPostsByTag(pageable,tag);
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

    public List<Post> getInactivePostByUser(int offset, int limit, String email) {
        Pageable pageable = PageRequest.of(offset, offset + limit);
        return postPageRepository.findInactivePostByUserId(pageable, email);
    }

    public List<Post> getPostByUserAndModerationStatus(int offset, int limit, String email, ModerationStatus status) {
        Pageable pageable = PageRequest.of(offset, offset + limit);
        return postPageRepository.findPostsByUserId(pageable, email, status);
    }

    public List<Post> getNewPostsForModeration(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, offset + limit);
        return postPageRepository.findAllNewForModeration(pageable);
    }

    public List<Post> getPostForModeration(int offset, int limit, ModerationStatus moderationStatus, String email) {
        Pageable pageable = PageRequest.of(offset, limit);
        return postPageRepository.findAllForModeration(pageable, moderationStatus, email);
    }

    public int getCountForModeration(){
        return (int) postRepository.countOfModeration();
    }

    public int getCountForModerationByStatusAndUser(ModerationStatus status, String name) {
        return (int) postRepository.countOfPostByUserAndModerationStatus(status,name);
    }
}


