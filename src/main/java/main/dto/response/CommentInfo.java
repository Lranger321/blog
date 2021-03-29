package main.dto.response;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentInfo {

    private long id;

    private long timestamp;

    private String text;

    private UserDtoResponse user;

}
