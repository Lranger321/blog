package main.dto.responce;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.dto.responce.UserDtoResponse;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PostViewResponse {

    private int id;
    private long timestamp;
    private UserDtoResponse user;
    private String title;
    private String text;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private List<CommentInfo> comments;
    private List<String> tags;

}
