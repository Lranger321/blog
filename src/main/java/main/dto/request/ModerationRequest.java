package main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModerationRequest {
    @JsonProperty("post_id")
    private Long postId;

    private String decision;

}
