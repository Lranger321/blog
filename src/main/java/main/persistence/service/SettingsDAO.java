package main.persistence.service;

import com.google.gson.Gson;
import main.persistence.repository.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SettingsDAO {

    @Autowired
    GlobalSettingsRepository repository;

    public HashMap<String,Boolean>  getSettings(){
        HashMap<String,Boolean> settings = new HashMap<>();
        repository.findAll().forEach(setting->{
            System.out.println(setting.getName() + "  "+ setting.getValue());
            settings.put(setting.getName(),setting.getValue());
        });
        return settings;
    }

}
