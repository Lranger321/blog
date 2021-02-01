package main.persistence.service;

import java.util.ArrayList;
import java.util.List;

import main.dto.PostDtoResponse;
import main.dto.PostsInfo;
import main.dto.UserDtoResponse;
import main.persistence.repository.PostForRequestRepository;
import main.persistence.repository.PostRepository;
import main.persistence.entity.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostDAO {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostForRequestRepository viewRepository;


    public PostsInfo getPost(int offset, int limit, String mode) {
        PostsInfo postsInfo = new PostsInfo();
        List<PostView> posts = (mode != null) ? getSortedPosts(mode) :
                getSortedPosts("recent");
        int count = posts.size();
        if(offset + limit < count){
            postsInfo.setPosts(createPostDtoList(posts.subList(offset, offset + limit)));
        }else{
            postsInfo.setPosts(createPostDtoList(posts.subList(offset, count)));
        }
        postsInfo.setCount(count);
        return postsInfo;
    }

    private List<PostView> getSortedPosts(String mode) {
        List<PostView> posts = new ArrayList<>();
        switch (mode) {
            //by  сортировать по дате публикации, выводить сначала новые (если
            // mode не задан, использовать это значение по умолчанию
            case "recent":
                posts = viewRepository.getAllRecent();
                break;
            // сортировать по убыванию количества комментариев (посты без комментариев выводить)
            case "popular":
                posts = viewRepository.getAllPopular();
                break;
            //- сортировать по убыванию количества лайков (посты без лайков дизлайков выводить)
            case "best":
                posts = viewRepository.getAllBest();
                break;
            //сортировать по дате публикации, выводить сначала стары
            case "early":
                posts = viewRepository.getAllEarly();
                break;
        }
        System.out.println(posts);
        return posts;
    }


    private List<PostDtoResponse> createPostDtoList(List<PostView> posts) {
        List<PostDtoResponse> responseList =  new ArrayList<>();
        posts.forEach(postView -> {
            UserDtoResponse user = new UserDtoResponse();
            user.setId(postView.getUserId());
            user.setName(postView.getName());
            PostDtoResponse post = new PostDtoResponse(postView.getId(), user, postView.getTitle(),
                    postView.getAnnounce(), postView.getLikeCount(), postView.getDislikeCount(),
                    postView.getCommentCount(), postView.getViewCount());
            responseList.add(post);
        });
        return responseList;
    }


}