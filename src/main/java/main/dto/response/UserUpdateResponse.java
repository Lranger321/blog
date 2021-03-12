package main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class UserUpdateResponse {

    private Boolean result;

    private HashMap<String,String> errors;

}
