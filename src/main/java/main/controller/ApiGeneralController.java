package main.controller;

import main.dto.request.CommentCreateRequest;
import main.dto.request.ModerationRequest;
import main.dto.request.SetSettingsRequest;
import main.dto.response.*;
import main.persistence.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitStorage initStorage;

    private final SettingsService settingsService;

    private final PostService postService;

    private final UserService userService;

    private final TagService tagService;

    private final ImageService imageService;

    private final PostGettingService postGettingService;

    @Autowired
    public ApiGeneralController(InitStorage initStorage, SettingsService settingsService,
                                PostService postService, UserService userService, TagService tagService, ImageService imageService, PostGettingService postGettingService) {
        this.initStorage = initStorage;
        this.settingsService = settingsService;
        this.postService = postService;
        this.userService = userService;
        this.tagService = tagService;
        this.imageService = imageService;
        this.postGettingService = postGettingService;
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
        return postGettingService.getCalendar(year);
    }

    @GetMapping("/tag")
    public TagStorage getTag() {
        return tagService.countAllWeight();
    }

    @PostMapping(value = "/image",produces="multipart/form-data")
    public ResponseEntity saveImage(MultipartFile multipartFile){
        imageService.saveImage(multipartFile);
        return null;
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/comment")
    public CommentCreateResponse commentCreate(@RequestBody CommentCreateRequest commentCreateRequest,
                                                               Principal principal){
        return postService.createComment(commentCreateRequest,principal.getName());
    }

    @PreAuthorize("hasAuthority('moder:write')")
    @PostMapping("/moderation")
    public ModerationResponse moderation(@RequestBody ModerationRequest request,Principal principal){
        return postService.moderation(request,principal.getName());
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/statistics/my")
    public StatisticsResponse my(Principal principal){
        return userService.getStatForUser(principal.getName());
    }

    @GetMapping("/statistics/all")
    public StatisticsResponse getAllStat(){
        return userService.getAllStat();
    }

    @PreAuthorize("hasAnyAuthority('moder:wrtite')")
    @PutMapping("/settings")
    public SetSettingsResponse setSettings(@RequestBody SetSettingsRequest request){
        return settingsService.setSettings(request);
    }

}
