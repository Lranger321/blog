package main.controller;


import main.dto.CalendarInfo;
import main.dto.TagStorage;
import main.persistence.service.PostService;
import main.dto.InitStorage;
import main.persistence.service.SettingsService;
import main.persistence.service.TagService;
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
    SettingsService settingsService;

    @Autowired
    PostService postService;

    @Autowired
    TagService tagService;

    public ApiGeneralController(InitStorage initStorage) {
        this.initStorage = initStorage;
    }

    @GetMapping("/init")
    public InitStorage init() {
        return initStorage;
    }

    @GetMapping("/settings")
    public HashMap<String, Boolean> getSettings() {
        return settingsService.getSettings();
    }

    @GetMapping("/calendar")
    public CalendarInfo getCalendar(String year) {
        return postService.getCalendar(year);
    }

    @GetMapping("/tag")
    public TagStorage getTag() {
        return tagService.countAllWeight();
    }

}
