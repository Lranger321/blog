package main.persistence;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import main.dto.CalendarInfo;
import main.dto.PostDtoResponse;
import main.dto.PostsInfo;
import main.dto.UserDtoResponse;
import main.persistence.entity.Post;
import main.persistence.entity.Vote;
import main.persistence.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostDAO {

    @Autowired
    PostRepository postRepository;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public List<PostDtoResponse> getPosts(String mode) {
        List<Post> posts = (mode != null) ? getSortedPosts(mode) :
                getSortedPosts("recent");
        return createPostDtoList(posts);
    }

    private List<Post> getSortedPosts(String mode) {
        List<Post> posts = postRepository.findAll().stream()
                .filter(post -> post.getActive() && post.getModerationStatus().toString().equals("ACCEPTED"))
                .collect(Collectors.toList());
        switch (mode) {
            //by  сортировать по дате публикации, выводить сначала новые (если
            // mode не задан, использовать это значение по умолчанию
            case "recent":
                posts.sort(Comparator.comparing(Post::getTime));
                break;
            // сортировать по убыванию количества комментариев (посты без комментариев выводить)
            case "popular":
                posts.sort(Comparator.comparingInt(post -> post.getComments().size()));
                break;
            //- сортировать по убыванию количества лайков (посты без лайков дизлайков выводить)
            case "best":
                posts.sort(Comparator.comparingInt(post ->
                        (int) post.getVotes().stream().
                                filter(vote -> !vote.getValue()).count()));
                break;
            //сортировать по дате публикации, выводить сначала старые
            case "early":
                posts.sort(((post, post1) -> post1.getTime().compareTo(post.getTime())));
                break;
        }
        return posts;
    }

    public CalendarInfo calendarInfo(String inputYear) {
        if (inputYear == null) {
            inputYear = dateFormat.format(new Date()).split("-")[0];
        }
        CalendarInfo calendarInfo = new CalendarInfo();
        List<Integer> years = new ArrayList<>();
        HashMap<String, Integer> postsCalendar = new HashMap<>();
        String finalInputYear = inputYear;
        postRepository.getAllActiveAndAccepted().forEach(post -> {
            String date = dateFormat.format(post.getTime());
            int year = Integer.parseInt(date.split("-")[0]);
            if (!years.contains(year)) {
                years.add(year);
            }
            if (Integer.toString(year).equals(finalInputYear)) {
                postsCalendar.put(date, postsCalendar.getOrDefault(date, 0) + 1);
            }
        });
        calendarInfo.setPosts(postsCalendar);
        calendarInfo.setYears(years);
        return calendarInfo;
    }

    public List<PostDtoResponse>  getPostByDate(String date) {
        List<PostDtoResponse> posts = createPostDtoList(getSortedPosts("recent"));
        posts.removeIf(post -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(post.getTimestamp() * 1000);
            String postDate = dateFormat.format(calendar.getTime());
            return !(postDate.equals(date));
        });
        return posts;
    }

    public List<PostDtoResponse> getPostByTag(String tag) {
        List<PostDtoResponse> posts =createPostDtoList(postRepository.findAll().stream()
                        .filter(post -> post.getTagList().contains(tag))
                        .collect(Collectors.toList()));
        return posts;
    }


    private List<PostDtoResponse> createPostDtoList(List<Post> posts) {
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

}
