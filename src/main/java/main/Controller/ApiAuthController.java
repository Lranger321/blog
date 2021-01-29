package main.Controller;

import com.google.gson.Gson;
import main.Database.SettingsDAO;
import main.Database.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Controller;
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
    public String authCheck(HttpSession session){
        System.out.println(userDAO.checkAuth(session.getId()));
        return userDAO.checkAuth(session.getId());
    }


}
