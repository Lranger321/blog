package main.persistence.service;

import lombok.RequiredArgsConstructor;
import main.dto.request.CommentCreateRequest;
import main.dto.request.ModerationRequest;
import main.dto.request.PostCreateRequest;
import main.dto.request.VoteRequest;
import main.dto.response.CommentCreateResponse;
import main.dto.response.ModerationResponse;
import main.dto.response.PostCreateResponse;
import main.dto.response.VoteResponse;
import main.persistence.entity.*;
import main.persistence.repository.*;
import main.persistence.service.converters.EntityConverter;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final TagRepository tagRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final EntityConverter entityConverter;

    private final VotesRepository votesRepository;

    private final GlobalSettingsRepository settingsRepository;

    public PostCreateResponse updatePost(PostCreateRequest request, long id, String email) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            User userWhoUpdate = userRepository.findByEmail(email).get();
            if (!post.get().getUser().getEmail().equals(email) && !userWhoUpdate.isModerator()) {
                return new PostCreateResponse(false, null);
            }
        }
        return savePost(post.get(), request, email);
    }

    public PostCreateResponse createPost(PostCreateRequest request, String email) {
        return savePost(new Post(), request, email);
    }

    private PostCreateResponse savePost(Post post, PostCreateRequest request, String email) {
        HashMap<String, String> errors = getPostCreateErrors(request);
        boolean result = false;
        User user = userRepository.findByEmail(email).orElse(null);
        if (errors == null && user != null) {
            System.out.println(request);
            post.setIsActive(request.getActive() == 1);
            post.setUser(user);
            post.setTime(new Date(request.getTimestamp() * 1000));
            post.setTagList(createTagList(request.getTags()));
            post.setTitle(request.getTitle());
            post.setText(request.getText());
            if (settingsRepository.findByCode("POST_PREMODERATION").get().getValue()) {
                post.setModerationStatus(ModerationStatus.NEW);
            } else {
                post.setModerationStatus(ModerationStatus.ACCEPTED);
            }
            postRepository.save(post);
            result = true;
        }
        return new PostCreateResponse(result, errors);
    }

    private List<Tag> createTagList(List<String> tags) {
        List<Tag> tagList = new ArrayList<>();
        for (String name : tags) {
            Tag tag = tagRepository.findByName(name).orElse(null);
            if (tag == null) {
                tag = Tag.builder()
                        .name(name)
                        .build();
                tagRepository.save(tag);
                tag = tagRepository.findByName(name).get();
            }
            tagList.add(tag);
        }
        return tagList;
    }

    private HashMap<String, String> getPostCreateErrors(PostCreateRequest request) {
        HashMap<String, String> errors = new HashMap<>();
        if (request.getTitle() == null) {
            errors.put("title", "Заголовок не установлен");
        }
        if (Jsoup.parse(request.getText()).text().length() < 20) {
            errors.put("text", "Текст публикации слишком короткий");
        }
        System.out.println(errors);
        if (errors.keySet().isEmpty()) {
            return null;
        }
        return errors;
    }

    public CommentCreateResponse createComment(CommentCreateRequest commentCreateRequest, String email) {
        CommentCreateResponse response = new CommentCreateResponse();
        HashMap<String, String> errors = createCommentErrors(commentCreateRequest);
        if (errors.keySet().isEmpty()) {
            Optional<Post> optionalPost = postRepository.findById(commentCreateRequest.getPostId());
            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();
                Comment comment = entityConverter.createComment(commentCreateRequest, email, post);
                List<Comment> comments = post.getComments();
                comments.add(comment);
                post.setComments(comments);
                response.setId(commentRepository.save(comment).getId());
                postRepository.save(post);
            }
        } else {
            response.setResult(false);
            response.setErrors(errors);
        }
        return response;
    }

    public ModerationResponse moderation(ModerationRequest request, String email) {
        Optional<Post> postOptional = postRepository.findById(request.getPostId());
        Optional<User> moderatorOptional = userRepository.findByEmail(email);
        if (postOptional.isPresent() && moderatorOptional.isPresent()) {
            Post post = postOptional.get();
            User moderator = moderatorOptional.get();
            switch (request.getDecision()) {
                case "new":
                    post.setModerationStatus(ModerationStatus.NEW);
                    break;
                case "accept":
                    post.setModerationStatus(ModerationStatus.ACCEPTED);
                    break;
                case "decline":
                    post.setModerationStatus(ModerationStatus.DECLINED);
                    break;
            }
            post.setModerator(moderator);
            postRepository.save(post);
            return new ModerationResponse(true);
        }
        return new ModerationResponse(false);
    }

    private HashMap<String, String> createCommentErrors(CommentCreateRequest commentCreateRequest) {
        HashMap<String, String> errors = new HashMap<>();
        if (Jsoup.parse(commentCreateRequest.getText()).text().length() < 2) {
            errors.put("text", "Текст комментария не задан или слишком короткий");
        }
        return errors;
    }

    public VoteResponse setVote(VoteRequest request, String email, int value) {
        User user;
        try {
            user = userRepository.findByEmail(email).
                    orElseThrow((() -> new UserPrincipalNotFoundException("User not found")));
        } catch (UserPrincipalNotFoundException e) {
            e.printStackTrace();
            return new VoteResponse(false);
        }
        Optional<Post> postOptional = postRepository.findById(request.getPostId());
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            Vote vote = votesRepository.findVoteByPostAndUser(email, post.getId()).orElse(new Vote());
            vote.setTime(new Date());
            vote.setValue(value);
            vote.setUser(user);
            vote.setPost(post);
            List votes = post.getVotes();
            votes.add(votesRepository.save(vote));
            post.setVotes(votes);
            postRepository.save(post);
        }
        return new VoteResponse(true);
    }

}
