package main.controller;

import main.dto.request.*;
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

    private final UserUpdateService userUpdateService;

    @Autowired
    public ApiGeneralController(InitStorage initStorage, SettingsService settingsService,
                                PostService postService, UserService userService, TagService tagService,
                                ImageService imageService, PostGettingService postGettingService, UserUpdateService userUpdateService) {
        this.initStorage = initStorage;
        this.settingsService = settingsService;
        this.postService = postService;
        this.userService = userService;
        this.tagService = tagService;
        this.imageService = imageService;
        this.postGettingService = postGettingService;
        this.userUpdateService = userUpdateService;
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

    @PostMapping(value = "/image", produces = "multipart/form-data")
    public ResponseEntity saveImage(MultipartFile multipartFile) {
        return imageService.saveImage(multipartFile);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/comment")
    public CommentCreateResponse commentCreate(@RequestBody CommentCreateRequest commentCreateRequest,
                                               Principal principal) {
        return postService.createComment(commentCreateRequest, principal.getName());
    }

    @PreAuthorize("hasAuthority('moder:write')")
    @PostMapping("/moderation")
    public ModerationResponse moderation(@RequestBody ModerationRequest request, Principal principal) {
        return postService.moderation(request, principal.getName());
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/statistics/my")
    public StatisticsResponse my(Principal principal) {
        return userService.getStatForUser(principal.getName());
    }

    @GetMapping("/statistics/all")
    public ResponseEntity getAllStat(Principal principal) {
        return userService.getAllStat(principal.getName());
    }

    @PreAuthorize("hasAnyAuthority('moder:wrtite')")
    @PutMapping("/settings")
    public SetSettingsResponse setSettings(@RequestBody SetSettingsRequest request) {
        return settingsService.setSettings(request);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @RequestMapping(value = "/profile/my", method = RequestMethod.POST,
            produces = "application/json")
    public UserUpdateResponse updateUser(@RequestBody UserUpdateRequest request, Principal principal) {
        return userUpdateService.updateUser(request, principal.getName());
    }

    @PreAuthorize("hasAuthority('user:write')")
    @RequestMapping(value = "/profile/my", method = RequestMethod.POST,
            produces = {"multipart/form-data"})
    public UserUpdateResponse updateUserWithPhoto(@RequestBody UserUpdateRequest request,Principal principal){
        System.out.println(principal.getName());
        return userUpdateService.updateUser(request,principal.getName());
    }



}
