package main.persistence.service;

import main.persistence.repository.UserRepository;
import main.persistence.entity.User;
import main.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserDAO {

    @Autowired
    UserRepository repository;

    HashMap<String, User> session = new HashMap<>();

    public AuthResponse checkAuth(String id) {
        AuthResponse response = new AuthResponse();
        if (session.containsKey(id)) {
            response.setResult(true);
            return response;
        } else {
            response.setResult(false);
            return response;
        }
    }


}
