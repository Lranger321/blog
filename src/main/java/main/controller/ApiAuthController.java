package main.controller;

import main.dto.request.AuthRequest;
import main.dto.request.UserRequest;
import main.dto.response.AuthResponse;
import main.dto.response.CaptchaResponse;
import main.dto.response.RegisterDto;
import main.persistence.service.CaptchaService;
import main.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserService userService;

    private final CaptchaService captchaService;

    @Autowired
    public ApiAuthController(UserService userService, CaptchaService captchaService) {
        this.userService = userService;
        this.captchaService = captchaService;
    }


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
