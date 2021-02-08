package main.persistence.service;

import main.persistence.dao.SettingsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SettingsService {

    @Autowired
    SettingsDAO settingsDAO;

    public HashMap<String,Boolean> getSettings(){
        HashMap<String,Boolean> settings = new HashMap<>();
        settingsDAO.getSettings().forEach(setting->{
            settings.put(setting.getName(),setting.getValue());
        });
        return settings;
    }

}
