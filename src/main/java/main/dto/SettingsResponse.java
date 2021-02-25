package main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsResponse {

    @JsonProperty("MULTIUSER_MODE")
    private Boolean multiUserMode;

    @JsonProperty("POST_PREMODERATION")
    private Boolean postPreModeration;

    @JsonProperty("STATISTICS_IS_PUBLIC")
    private Boolean statisticsIsPublic;

    public Boolean getMultiUserMode() {
        return multiUserMode;
    }

    public void setMultiUserMode(Boolean multiUserMode) {
        this.multiUserMode = multiUserMode;
    }

    public Boolean getPostPreModeration() {
        return postPreModeration;
    }

    public void setPostPreModeration(Boolean postPreModeration) {
        this.postPreModeration = postPreModeration;
    }

    public Boolean getStatisticsIsPublic() {
        return statisticsIsPublic;
    }

    public void setStatisticsIsPublic(Boolean statisticsIsPublic) {
        this.statisticsIsPublic = statisticsIsPublic;
    }
}
