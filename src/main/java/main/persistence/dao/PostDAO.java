package main.persistence.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import main.dto.CalendarInfo;
import main.persistence.entity.Post;
import main.persistence.entity.Tag;
import main.persistence.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostDAO {

    @Autowired
    PostRepository postRepository;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public List<Post> getPosts(String mode) {
        return (mode != null) ? getSortedPosts(mode) :
                getSortedPosts("recent");
    }

    private List<Post> getSortedPosts(String mode) {
        List<Post> posts = null;
        switch (mode) {
            //by  сортировать по дате публикации, выводить сначала новые (если
            // mode не задан, использовать это значение по умолчанию
            case "recent":
                posts = postRepository.getAllSortedByDateAsc();
                break;
            // сортировать по убыванию количества комментариев (посты без комментариев выводить)
            case "popular":
                posts = postRepository.getAllSortedByComments();
                break;
            //- сортировать по убыванию количества лайков (посты без лайков дизлайков выводить)
            case "best":
                posts = postRepository.getAllSortedByLikes();
                break;
            //сортировать по дате публикации, выводить сначала старые
            case "early":
                posts = postRepository.getAllSortedByDate();
                break;
        }
        return posts;
    }

    public List<Post> getPostByDate(String date) {
        List<Post> posts = getSortedPosts("recent");
        posts.removeIf(post -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(post.getTime().getTime());
            String postDate = dateFormat.format(calendar.getTime());
            return !(postDate.equals(date));
        });
        return posts;
    }

    public List<Post> getPostByTag(String tag) {
        return postRepository.findAll().stream()
                .filter(post -> {
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

    public Post getPostById(int id){
        return postRepository.getOne(id);
    }

}


