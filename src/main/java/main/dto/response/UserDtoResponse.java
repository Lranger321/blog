package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDtoResponse {

    private int id;

    private String name;

    private String photo;

    private String email;

    private Boolean settings;

    private Boolean moderation;

    private int moderationCount;

}
