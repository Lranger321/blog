package main.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostsInfo {

    private Long count;

    private List<PostDtoResponse> posts = new ArrayList<>();

}
