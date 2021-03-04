package main.controller;

import main.dto.response.CalendarInfo;
import main.dto.response.SettingsResponse;
import main.dto.response.TagStorage;
import main.dto.response.InitStorage;
import main.persistence.service.PostService;
import main.persistence.service.SettingsService;
import main.persistence.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitStorage initStorage;

    private final SettingsService settingsService;

    private final PostService postService;

    private final TagService tagService;

    @Autowired
    public ApiGeneralController(InitStorage initStorage, SettingsService settingsService,
                                PostService postService, TagService tagService) {
        this.initStorage = initStorage;
        this.settingsService = settingsService;
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/init")
    public InitStorage init() {
        return initStorage;
    }

    @GetMapping("/settings")
    public SettingsResponse getSettings() {
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
