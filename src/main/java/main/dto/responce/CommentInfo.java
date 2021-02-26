package main.dto.responce;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.dto.responce.UserDtoResponse;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CommentInfo {

    private int id;

    private long timestamp;

    private String text;

    private UserDtoResponse user;

}
