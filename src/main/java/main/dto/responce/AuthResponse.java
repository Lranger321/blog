package main.dto.responce;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import main.dto.responce.UserDtoResponse;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private boolean result;

    private UserDtoResponse user;

}
