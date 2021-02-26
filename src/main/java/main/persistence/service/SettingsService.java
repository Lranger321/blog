package main.persistence.service;

import main.dto.responce.SettingsResponse;
import main.persistence.dao.SettingsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private final SettingsDAO settingsDAO;

    @Autowired
    public SettingsService(SettingsDAO settingsDAO) {
        this.settingsDAO = settingsDAO;
    }

    public SettingsResponse getSettings() {
        SettingsResponse response = new SettingsResponse();
        settingsDAO.getSettings().forEach(setting -> {
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
