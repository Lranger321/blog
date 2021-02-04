package main.controller;


import main.dto.CalendarInfo;
import main.dto.PostsInfo;
import main.persistence.service.PostService;
import main.persistence.SettingsDAO;
import main.dto.InitStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitStorage initStorage;

    @Autowired
    SettingsDAO settingsDAO;

    @Autowired
    PostService postService;

    public ApiGeneralController(InitStorage initStorage) {
        this.initStorage = initStorage;
    }

    @GetMapping("/init")
    public InitStorage init() {
        return initStorage;
    }

    @GetMapping("/settings")
    public HashMap<String, Boolean> getSettings() {
        return settingsDAO.getSettings();
    }

    @GetMapping("/calendar")
    public CalendarInfo getCalendar(String year) {
        return postService.getCalendar(year);
    }

    @GetMapping("/tag")
    public String getTag() {
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
    public PostsInfo searchPosts(int offset, int limit, String mode) {
        return postService.getPosts(offset, limit, mode);
    }

}
