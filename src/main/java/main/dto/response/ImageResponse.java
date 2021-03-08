package main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class ImageResponse {

    private boolean result;

    private HashMap<String,String> errors;

}
