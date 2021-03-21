package main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CommentCreateRequest {
    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("post_id")
    private int postId;

    private String text;
}
