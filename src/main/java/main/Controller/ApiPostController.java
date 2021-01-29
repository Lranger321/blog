package main.Controller;

import main.Database.PostDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity getPosts(int offset, int limit, String mode){
        String response = postDAO.getPost(offset,limit,mode);
        return new ResponseEntity(response,HttpStatus.OK);
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

}
