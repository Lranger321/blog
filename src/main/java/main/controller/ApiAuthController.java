package main.controller;

import main.dto.AuthResponse;
import main.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

//Controller for /api/auth/*
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    UserService userService;

    @GetMapping("/check")
    public AuthResponse authCheck(HttpSession session){
        return userService.checkAuth(session.getId());
    }

}
