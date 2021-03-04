package main.persistence.service;

import main.dto.request.AuthRequest;
import main.dto.response.*;
import main.persistence.dao.CaptchaDAO;
import main.persistence.dao.PostDAO;
import main.persistence.dao.UserDAO;
import main.persistence.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;

@Service
public class UserService {

    private final UserDAO userDAO;

    private final CaptchaDAO captchaDAO;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final PostDAO postDAO;

    public UserService(UserDAO userDAO, CaptchaDAO captchaDAO,
                       AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, PostDAO postDAO) {
        this.userDAO = userDAO;
        this.captchaDAO = captchaDAO;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.postDAO = postDAO;
    }


    public AuthResponse checkAuth(Principal principal) {
        AuthResponse authResponse;
        if (principal != null) {
            User user = userDAO.getUserByEmail(principal.getName());
            int moderationCount = (user.isModerator()) ? postDAO.getCountForModeration() : 0;
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
        User userInDB = userDAO.getUserByEmail(request.getEmail());
        int moderationCount = (userInDB.isModerator()) ? postDAO.getCountForModeration() : 0;
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
            userDAO.saveUser(user);
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
        if (userDAO.getUserByEmail(email) != null) {
            errors.put("e_mail", "Этот e-mail уже зарегистрирован");
        }
        if (password.length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (!captchaDAO.getCaptcha(captchaSecret).getCode().equals(captcha)) {
            errors.put("captcha", "Код с картинки введён неверно");
        }
        if(name.length() < 3){
            errors.put("name","Имя указано неверно");
        }
        System.out.println(errors.keySet().size());
        return errors;
    }

}
