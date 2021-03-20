package main.controller;

import main.dto.request.AuthRequest;
import main.dto.request.ChangePasswordRequest;
import main.dto.request.PasswordRestoreRequest;
import main.dto.request.UserRequest;
import main.dto.response.*;
import main.persistence.service.CaptchaService;
import main.persistence.service.UserService;
import main.persistence.service.UserUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserService userService;

    private final CaptchaService captchaService;

    private final UserUpdateService userUpdateService;

    @Autowired
    public ApiAuthController(UserService userService, CaptchaService captchaService, UserUpdateService userUpdateService) {
        this.userService = userService;
        this.captchaService = captchaService;
        this.userUpdateService = userUpdateService;
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
        return userService.userRegister(user);
    }

    @PostMapping("/restore")
    public PasswordRestoreResponse restore(@RequestBody PasswordRestoreRequest restoreRequest){
        return userUpdateService.passwordRestore(restoreRequest);
    }

    @PostMapping("/password")
    public ChangePasswordResponse changePassword(@RequestBody ChangePasswordRequest request,Principal principal){
        return userUpdateService.changePassword(request,principal.getName());
    }

}
