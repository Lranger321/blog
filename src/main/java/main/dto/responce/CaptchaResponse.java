package main.dto.responce;

import lombok.Data;

@Data
public class CaptchaResponse {

    private String secret;

    private String image;

}
