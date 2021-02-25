package main.controller;

import main.dto.*;
import main.persistence.service.CaptchaService;
import main.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> authCheck(Principal principal){
        return ResponseEntity.ok(userService.checkAuth(principal));
    }

    @GetMapping("/captcha")
    public CaptchaResponse createCaptcha(){
        return captchaService.createCaptcha();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
       AuthResponse authResponse = userService.login(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(value = "/register",produces = "application/json")
    public RegisterDto userRegister(@RequestBody UserRequest user){
        return userService.userRegister(user.getEmail(), user.getPassword(), user.getName(), user.getCaptcha(),
                user.getCaptchaSecret());
    }

}
