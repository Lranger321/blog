package main.persistence.dao;

import java.util.List;
import main.persistence.entity.GlobalSetting;
import main.persistence.repository.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SettingsDAO {

    private final GlobalSettingsRepository repository;

    @Autowired
    public SettingsDAO(GlobalSettingsRepository repository) {
        this.repository = repository;
    }

    public List<GlobalSetting> getSettings() {
        return (List<GlobalSetting>) repository.findAll();
    }

}
