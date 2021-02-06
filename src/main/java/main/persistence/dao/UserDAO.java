package main.persistence.dao;

import main.persistence.repository.UserRepository;
import main.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserDAO {

    @Autowired
    UserRepository repository;

    HashMap<String, User> session = new HashMap<>();

    public User checkAuth(String id) {
        User user = null;
        if(session.containsKey(id)) {
            user = session.get(id);
        }
        return user;
    }

}
