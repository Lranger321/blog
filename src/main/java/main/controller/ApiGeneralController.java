package main.controller;


import main.persistence.service.SettingsDAO;
import main.dto.InitStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitStorage initStorage;

    @Autowired
    SettingsDAO settingsDAO;

    public ApiGeneralController(InitStorage initStorage){
        this.initStorage = initStorage;
    }

    @GetMapping("/init")
    public InitStorage init(){
        return initStorage;
    }

    @GetMapping("/settings")
    public HashMap<String, Boolean> getSettings(){
        System.out.println(settingsDAO.getSettings());
        return settingsDAO.getSettings();
    }

    @GetMapping
    public String getCalendar(){

        return null;
    }

}
