package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterDto {

    private Boolean result;

    private HashMap<String,String> errors;

}
