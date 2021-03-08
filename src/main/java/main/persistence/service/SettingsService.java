package main.persistence.service;

import main.dto.response.SettingsResponse;
import main.persistence.repository.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//todo убрать DAO
@Service
public class SettingsService {

    private final GlobalSettingsRepository repository;

    @Autowired
    public SettingsService(GlobalSettingsRepository repository) {
        this.repository = repository;
    }

    public SettingsResponse getSettings() {
        SettingsResponse response = new SettingsResponse();
        repository.findAll().forEach(setting -> {
            if (setting.getCode().equals("STATISTICS_IS_PUBLIC")) {
                response.setStatisticsIsPublic(setting.getValue());
            } else if (setting.getCode().equals("MULTIUSER_MODE")) {
                response.setMultiUserMode(setting.getValue());
            } else if (setting.getCode().equals("POST_PREMODERATION")) {
                response.setMultiUserMode(setting.getValue());
            }
        });
        return response;
    }

}
