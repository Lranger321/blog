package main.persistence.service;

import main.dto.request.CommentCreateRequest;
import main.persistence.entity.Comment;
import main.persistence.entity.Post;
import main.persistence.repository.CommentRepository;
import main.persistence.repository.TagRepository;
import main.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EntityConverter {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final TagRepository tagRepository;

    @Autowired
    public EntityConverter(CommentRepository commentRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }


    public Comment createComment(CommentCreateRequest commentCreateRequest, String email, Post post) {
        Comment comment = Comment.builder()
                .user(userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")))
                .text(commentCreateRequest.getText())
                .time(new Date())
                .post(post)
                .build();
        if (commentCreateRequest.getParentId() != null) {
            comment.setParent(commentRepository.findById(Integer.valueOf(commentCreateRequest.getParentId()))
                    .orElse(null));
        }
        return comment;
    }

}
