package main.persistence.service;

import main.dto.request.CommentCreateRequest;
import main.dto.request.ModerationRequest;
import main.dto.request.PostCreateRequest;
import main.dto.request.VoteRequest;
import main.dto.response.*;
import main.persistence.entity.*;
import main.persistence.repository.*;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class PostService {

    private final TagRepository tagRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final EntityConverter entityConverter;

    private final VotesRepository votesRepository;

    @Autowired
    public PostService(TagRepository tagRepository, PostRepository postRepository,
                       UserRepository userRepository, CommentRepository commentRepository,
                       EntityConverter entityConverter, VotesRepository votesRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.entityConverter = entityConverter;
        this.votesRepository = votesRepository;
    }

    public PostCreateResponse updatePost(PostCreateRequest request, long id, String email) {
        Post post = postRepository.findById(id);
        if (post.getUser().getEmail().equals(email)) {
            return new PostCreateResponse(false, null);
        }
        return savePost(post, request, email);
    }

    public PostCreateResponse createPost(PostCreateRequest request, String email) {
        return savePost(new Post(), request, email);
    }

    private PostCreateResponse savePost(Post post, PostCreateRequest request, String email) {
        HashMap<String, String> errors = getPostCreateErrors(request);
        boolean result = false;
        User user = userRepository.findByEmail(email).orElse(null);
        System.out.println(errors);
        if (errors == null && user != null) {
            post.setActive(request.isActive());
            post.setUser(user);
            post.setTime(new Date(request.getTimestamp() / 1000));
            post.setTagList(createTagList(request.getTags()));
            post.setTitle(request.getTitle());
            post.setText(request.getText());
            post.setModerationStatus(ModerationStatus.NEW);
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
                tag = new Tag();
                tag.setName(name);
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
        if (errors.keySet().isEmpty()) {
            return null;
        }
        return errors;
    }

    public CommentCreateResponse createComment(CommentCreateRequest commentCreateRequest, String email) {
        CommentCreateResponse response = new CommentCreateResponse();
        HashMap<String, String> errors = createCommentErrors(commentCreateRequest);
        if (errors.keySet().isEmpty()) {
            Post post = postRepository.findById(commentCreateRequest.getPostId());
            Comment comment = entityConverter.createComment(commentCreateRequest, email, post);
            List<Comment> comments = post.getComments();
            comments.add(comment);
            post.setComments(comments);
            response.setId(commentRepository.save(comment).getId());
            postRepository.save(post);
        } else {
            response.setResult(false);
            response.setErrors(errors);
        }
        return response;
    }

    public ModerationResponse moderation(ModerationRequest request, String email) {
        Post post = postRepository.findById(request.getPostId());
        User moderator = userRepository.findByEmail(email).orElse(null);
        System.out.println(post + " " + moderator);
        if (post != null && moderator != null) {
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
        Post post = postRepository.findById(request.getPostId()).orElse(null);
        Vote vote = votesRepository.findVoteByPostAndUser(email, post.getId()).orElse(new Vote());
        vote.setTime(new Date());
        vote.setValue(value);
        vote.setUser(user);
        vote.setPost(post);
        List votes = post.getVotes();
        votes.add(votesRepository.save(vote));
        post.setVotes(votes);
        postRepository.save(post);
        return new VoteResponse(true);
    }

}
