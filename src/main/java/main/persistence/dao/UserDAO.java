package main.persistence.dao;

import main.persistence.repository.UserRepository;
import main.persistence.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserDAO{

    private final UserRepository userRepository;

    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

}
