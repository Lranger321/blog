package main.controller;

import main.dto.PostsInfo;
import main.persistence.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//Controller for /api/post/*
@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    @Autowired
    PostService postService;

    @GetMapping("")
    public PostsInfo getPosts(int offset, int limit, String mode){
        return postService.getPosts(offset,limit,mode);
    }

    @GetMapping("/byDate")
    public PostsInfo getPostsByDate(int offset,int limit,String date){
        return postService.getPostByDate(offset, limit, date);
    }

    @GetMapping("/byTag")
    public PostsInfo getPostByTag(int offset,int limit,String tag){
        return postService.getPostByTag(offset,limit,tag);
    }

}
