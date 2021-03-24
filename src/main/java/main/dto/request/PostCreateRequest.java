package main.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PostCreateRequest {
    private long timestamp;
    private int active;
    private List<String> tags;
    private String text;
    private String title;
}
