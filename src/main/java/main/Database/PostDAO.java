package main.Database;

import java.util.ArrayList;
import java.util.List;

import main.Entity.PostView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostDAO {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostForRequestRepository viewRepository;


    public String getPost(int offset, int limit, String mode) {
        List<PostView> posts = (mode != null) ? getSortedPosts(mode) :
                getSortedPosts("recent");
        int counts = posts.size();
        //create Json
        JSONObject response = new JSONObject();
        JSONArray jsonArray = postsToJson(posts.subList(offset, offset + limit));
        response.put("counts", counts);
        response.put("posts", jsonArray);
        return response.toJSONString();
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


    private JSONArray postsToJson(List<PostView> posts) {
        JSONArray jsonArray = new JSONArray();
        posts.forEach(postView -> {
            JSONObject post = new JSONObject();
            post.put("id", postView.getId());
            post.put("timestamp", postView.getTimestamp());
            post.put("title", postView.getTitle());
            post.put("announce", postView.getAnnounce());
            post.put("likeCount", postView.getLikeCount());
            post.put("dislikeCount", postView.getDislikeCount());
            post.put("commentCount", postView.getCommentCount());
            post.put("viewCount", postView.getViewCount());
            JSONObject user = new JSONObject();
            user.put("id", postView.getUserId());
            user.put("name", postView.getName());
            post.put("user", user);
            jsonArray.add(post);
        });
        return jsonArray;
    }


}
