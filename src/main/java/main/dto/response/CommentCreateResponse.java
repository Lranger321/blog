package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentCreateResponse {
    private Integer id;
    private Boolean result;
    private HashMap<String, String> errors;
}
