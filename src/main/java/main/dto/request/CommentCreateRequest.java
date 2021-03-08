package main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentCreateRequest {
    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("post_id")
    private Long postId;

    private String text;
}
