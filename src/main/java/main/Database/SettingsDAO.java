package main.Database;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SettingsDAO {

    @Autowired
    GlobalSettingsRepository repository;

    public String getSettings(){
        HashMap<String,Boolean> settings = new HashMap<>();
        repository.findAll().forEach(setting->{
            settings.put(setting.getName(),setting.getValue());
        });
        return new Gson().toJson(settings);
    }

}
