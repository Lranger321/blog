package main.Controller;


import com.google.gson.Gson;
import main.Database.SettingsDAO;
import main.Entity.InitStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String init(){
        return new Gson().toJson(initStorage,InitStorage.class);
    }

    @GetMapping("/settings")
    public String getSettings(){
        System.out.println(settingsDAO.getSettings());
        return settingsDAO.getSettings();
    }

}
