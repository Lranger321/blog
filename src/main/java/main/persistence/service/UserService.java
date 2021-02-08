package main.persistence.service;

import main.dto.AuthResponse;
import main.dto.RegisterDto;
import main.persistence.dao.UserDAO;
import main.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    public AuthResponse checkAuth(String id) {
        AuthResponse authResponse;
        User user = userDAO.checkAuth(id);
        if (user == null) {
            authResponse = Converter.createAuthResponse(false);
        } else {
            authResponse = Converter.createAuthResponse(true, user);
        }
        return authResponse;
    }

    public RegisterDto userRegister(String email, String password, String name, String captcha, String captchaSecret) {
        HashMap<String, String> errors = registerErrors(email, password, name, captcha, captchaSecret);
        RegisterDto registerDto = new RegisterDto();
        if (errors.keySet().size() == 0) {
            User user = new User();
            user.setEmail(email);
            user.setModerator(false);
            user.setName(name);
            user.setRegTime(new Date());
            user.setPassword(password);
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
        if (userDAO.getCountUserByEmail(email) != 0) {
            errors.put("e_mail", "Этот e-mail уже зарегистрирован");
        }
        if (password.length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (captchaSecret == null) {
            errors.put("captcha", "Код с картинки введён неверно");
        }
        /*if(name){
            errors.put("name","Имя указано неверно");
        }*/
        return errors;
}

}
