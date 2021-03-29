package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDtoResponse {

    private long id;

    private String name;

    private String photo;

    private String email;

    private Boolean settings;

    private Boolean moderation;

    private long moderationCount;

}
