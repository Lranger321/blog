package main.persistence.service;

import main.dto.request.SetSettingsRequest;
import main.dto.response.SetSettingsResponse;
import main.dto.response.SettingsResponse;
import main.persistence.entity.GlobalSetting;
import main.persistence.repository.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public SetSettingsResponse setSettings(SetSettingsRequest request){
            GlobalSetting multiUserMode = repository.findByCode("MULTIUSER_MODE").orElse(null);
            GlobalSetting preModeration = repository.findByCode("POST_PREMODERATION").orElse(null);
            GlobalSetting statIsPublic = repository.findByCode("STATISTICS_IS_PUBLIC").orElse(null);
            if(multiUserMode != null && preModeration !=null && statIsPublic != null) {
                multiUserMode.setValue(request.isMultiUserMode());
                preModeration.setValue(request.isPostPreModeration());
                statIsPublic.setValue(request.isStatisticsIsPublic());
                repository.save(multiUserMode);
                repository.save(preModeration);
                repository.save(statIsPublic);
                return new SetSettingsResponse(true);
            }
        return new SetSettingsResponse(false);
    }

}
