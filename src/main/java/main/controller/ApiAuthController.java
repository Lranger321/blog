package main.controller;

import main.dto.*;
import main.persistence.service.CaptchaService;
import main.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

//Controller for /api/auth/*
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    UserService userService;

    @Autowired
    CaptchaService captchaService;

    @GetMapping("/check")
    public AuthResponse authCheck(HttpSession session){
        return userService.checkAuth(session.getId());
    }

    @GetMapping("/captcha")
    public CaptchaResponse createCaptcha(){
        return captchaService.createCaptcha();
    }

    @PostMapping(value = "/register",produces = "application/json")
    public RegisterDto userRegister(@RequestBody UserRequest user){
        System.out.println(user.getEmail());
        return userService.userRegister(user.getEmail(), user.getPassword(), user.getName(), user.getCaptcha(),
                user.getCaptchaSecret());
    }

}
