package main.controller;

import main.dto.request.PostCreateRequest;
import main.dto.response.PostCreateResponse;
import main.dto.response.PostViewResponse;
import main.dto.response.PostsInfo;
import main.persistence.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostService postService;

    @Autowired
    public ApiPostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping("")
    public PostsInfo getPosts(int limit, int offset, String mode) {
        System.out.println(offset + "  " + limit);
        return postService.getPosts(offset, limit, mode);
    }

    @GetMapping("/byDate")
    public PostsInfo getPostsByDate(int offset, int limit, String date) {
        return postService.getPostByDate(offset, limit, date);
    }

    @GetMapping("/byTag")
    public PostsInfo getPostByTag(int offset, int limit, String tag) {
        return postService.getPostByTag(offset, limit, tag);
    }

    @GetMapping("/search")
    public PostsInfo searchPosts(int offset, int limit, String query) {
        return postService.searchPosts(offset, limit, query);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostViewResponse> getPostById(@PathVariable int id) {
        PostViewResponse postViewResponse = postService.getPostById(id);
        if (postViewResponse == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(postService.getPostById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("")
    public PostCreateResponse createPost(@RequestBody PostCreateRequest request, Principal principal) {
        return postService.createPost(request, principal.getName());
    }

    @PreAuthorize("hasAuthority('moder:write')")
    @GetMapping("/moderation")
    public PostsInfo getPostsForModeration(int offset, int limit, String status, Principal principal) {
        return postService.getPostsForModeration(offset, limit, status, principal.getName());
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/{id}")
    public PostCreateResponse updatePost(@RequestBody PostCreateRequest request, Principal principal,
                                         @PathVariable int id) {
        return postService.updatePost(request, id, principal.getName());
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/my")
    public PostsInfo getPostByUser(int offset, int limit, String status, Principal principal) {
        return postService.getPostByUser(offset, limit, status, principal.getName());
    }

}
