package main.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

//todo Add image
@Data
public class UserUpdateRequest {

    private String name;

    private String email;

    private String password;

    private MultipartFile photo;

    private Integer removePhoto;

}
