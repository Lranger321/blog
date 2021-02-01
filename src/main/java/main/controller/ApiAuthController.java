package main.controller;

import main.persistence.service.UserDAO;
import main.dto.AuthResponse;
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
    UserDAO userDAO;

    @GetMapping("/check")
    public AuthResponse authCheck(HttpSession session){
        System.out.println(userDAO.checkAuth(session.getId()));
        return userDAO.checkAuth(session.getId());
    }


}
