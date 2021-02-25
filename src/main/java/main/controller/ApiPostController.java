package main.controller;

import main.dto.PostViewResponse;
import main.dto.PostsInfo;
import main.persistence.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    @Autowired
    private PostService postService;

    @GetMapping("")
    public PostsInfo getPosts(int limit, int offset, String mode){
        System.out.println(offset+"  "+limit);
        return postService.getPosts(offset,limit,mode);
    }

    @GetMapping("/byDate")
    public PostsInfo getPostsByDate(int offset,int limit,String date){
        return postService.getPostByDate(offset, limit, date);
    }

    @GetMapping("/byTag")
    public PostsInfo getPostByTag(int offset,int limit,String tag){
        System.out.println(tag);
        return postService.getPostByTag(offset,limit,tag);
    }

    @GetMapping("/search")
    public PostsInfo searchPosts(int offset, int limit, String query) {
        System.out.println(offset+" "+limit+" "+query);
        return postService.searchPosts(offset, limit, query);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPostById(@PathVariable int id){
        System.out.println("POST ID:"+id);
        PostViewResponse postViewResponse = postService.getPostById(id);
        if(postViewResponse == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(postService.getPostById(id),HttpStatus.OK);
    }

}
