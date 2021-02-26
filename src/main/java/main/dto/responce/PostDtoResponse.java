package main.dto.responce;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.dto.responce.UserDtoResponse;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PostDtoResponse {

    private int id;

    private long timestamp;

    private UserDtoResponse user;

    private String title;

    private String announce;

    private int likeCount;

    private int dislikeCount;

    private int commentCount;

    private int viewCount;

}
