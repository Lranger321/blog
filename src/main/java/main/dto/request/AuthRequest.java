package main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthRequest {

    @JsonProperty("e_mail")
    private String email;
    private String password;

}
