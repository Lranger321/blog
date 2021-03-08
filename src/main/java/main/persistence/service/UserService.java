package main.persistence.service;

import main.dto.request.AuthRequest;
import main.dto.response.*;
import main.persistence.entity.User;
import main.persistence.repository.CaptchaRepository;
import main.persistence.repository.PostRepository;
import main.persistence.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;

//todo убрать DAO
@Service
public class UserService {

    private final CaptchaRepository captchaRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    public UserService(CaptchaRepository captchaRepository, AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder, UserRepository userRepository, PostRepository postRepository) {
        this.captchaRepository = captchaRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public AuthResponse checkAuth(Principal principal) {
        AuthResponse authResponse;
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            long moderationCount = (user.isModerator()) ? postRepository.countOfModeration() : 0;
            authResponse = Converter.createAuthResponse(true, user, moderationCount);
        } else {
            authResponse = Converter.createAuthResponse(false);
        }
        return authResponse;
    }

    public AuthResponse login(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User userInDB = userRepository.findByEmail(request.getEmail()).orElse(null);
        long moderationCount = (userInDB.isModerator()) ? postRepository.countOfModeration() : 0;
        return Converter.createAuthResponse(true, userInDB, moderationCount);
    }

    public RegisterDto userRegister(String email, String password, String name, String captcha,
                                    String captchaSecret) {
        System.out.println(captchaSecret);
        HashMap<String, String> errors = registerErrors(email, password, name, captcha, captchaSecret);
        System.out.println(captcha);
        System.out.println(errors.keySet().size());
        RegisterDto registerDto = new RegisterDto();
        if (errors.keySet().size() == 0) {
            System.out.println("REG");
            User user = new User();
            user.setEmail(email);
            user.setModerator(false);
            user.setName(name);
            user.setRegTime(new Date());
            user.setPassword(passwordEncoder.encode(password));
            user.setCode(captchaSecret);
            System.out.println(user.getPassword());
            userRepository.save(user);
            registerDto.setResult(true);
        } else {
            registerDto.setResult(false);
            registerDto.setErrors(errors);
        }
        return registerDto;
    }

    private HashMap<String, String> registerErrors(String email, String password, String name, String captcha,
                                                   String captchaSecret) {
        HashMap<String, String> errors = new HashMap<>();
        if (!userRepository.findByEmail(email).isPresent()) {
            errors.put("e_mail", "Этот e-mail уже зарегистрирован");
        }
        if (password.length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (!captchaRepository.findBySecretCode(captchaSecret).get().getCode().equals(captcha)) {
            errors.put("captcha", "Код с картинки введён неверно");
        }
        if(name.length() < 3){
            errors.put("name","Имя указано неверно");
        }
        System.out.println(errors.keySet().size());
        return errors;
    }

}
