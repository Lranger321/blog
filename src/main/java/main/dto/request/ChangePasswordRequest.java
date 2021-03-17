package main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String code;

    private String password;

    private String captcha;

    @JsonProperty("secret_code")
    private String secretCode;


}
