package main.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CommentInfo {

    private int id;

    private long timestamp;

    private String text;

    private UserDtoResponse user;

}
