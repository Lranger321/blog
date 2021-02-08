package main.persistence.dao;

import main.persistence.repository.UserRepository;
import main.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserDAO {

    @Autowired
    UserRepository userRepository;

    HashMap<String, User> session = new HashMap<>();

    public User checkAuth(String id) {
        User user = null;
        if(session.containsKey(id)) {
            user = session.get(id);
        }
        return user;
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public int getCountUserByEmail(String email){
        return userRepository.countByEmail(email);
    }

}
