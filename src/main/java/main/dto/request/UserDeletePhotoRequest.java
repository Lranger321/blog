package main.dto.request;

import lombok.Data;

@Data
public class UserDeletePhotoRequest {

    private String name;

    private String email;

    private String password;

    private String photo;

    private Integer removePhoto;

}
