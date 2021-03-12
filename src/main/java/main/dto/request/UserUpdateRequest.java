package main.dto.request;

import lombok.Data;

//todo Add image
@Data
public class UserUpdateRequest {

    private String name;

    private String email;

    private String password;

    private Integer removePhoto;

}
