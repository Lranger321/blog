package main.dto.response;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostViewResponse {

    private long id;
    private long timestamp;
    private UserDtoResponse user;
    private String title;
    private String text;
    private long likeCount;
    private long dislikeCount;
    private long viewCount;
    private List<CommentInfo> comments;
    private List<String> tags;

}
