package main.Database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserDAO {

    @Autowired
    UserRepository repository;

    HashMap<String, User> session = new HashMap<>();

    public String checkAuth(String id) {
        if (session.containsKey(id)) {
            StringBuilder request = new StringBuilder("{\"result\": false,");
            request.append("\"user\":" + new Gson().toJson(session.get("id"), User.class));
            return request.toString();
        } else {
            return "{\"result\": false}";
        }
    }


}
