package main.controller;

import main.persistence.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadController {

    private final ImageService imageService;

    @Autowired
    public UploadController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/upload/{url}", produces = MediaType.ALL_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String url) {
        return imageService.getImage(url);
    }

}
