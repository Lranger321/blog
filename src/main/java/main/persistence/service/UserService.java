package main.persistence.service;

import main.dto.AuthResponse;
import main.persistence.dao.UserDAO;
import main.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    public AuthResponse checkAuth(String id){
        AuthResponse authResponse;
        User user = userDAO.checkAuth(id);
        if(user == null){
            authResponse = Converter.createAuthResponse(false);
        }else{
            authResponse = Converter.createAuthResponse(true,user);
        }
        return authResponse;
    }

}
