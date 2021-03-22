package main.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostCreateRequest {
    private long timestamp;
    private int active;
    private List<String> tags;
    private String text;
    private String title;
}
