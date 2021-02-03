package main.controller;

import main.persistence.service.PostDAO;
import main.dto.PostsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//Controller for /api/post/*
@RestController
@RequestMapping("/api")
public class ApiPostController {

    @Autowired
    PostDAO postDAO;

    @GetMapping("/post")
    public PostsInfo getPosts(int offset,int limit,String mode){
        System.out.println("check response: "+postDAO.getPost(offset,limit,mode));
        return postDAO.getPost(offset,limit,mode);
    }

    @GetMapping("/tag")
    public String getTag(){
        return "{" +
                "\"tags\":" +
                "[" +
                "{\"name\":\"Java\", \"weight\":1}," +
                "{\"name\":\"Spring\", \"weight\":0.56}," +
                "{\"name\":\"Hibernate\", \"weight\":0.22}," +
                "{\"name\":\"Hadoop\", \"weight\":0.17}" +
                "]" +
                "}";
    }

    @GetMapping("/search")
    public PostsInfo searchPosts(int offset, int limit, String mode){
        return postDAO.getPost(offset,limit,mode);
    }

}
