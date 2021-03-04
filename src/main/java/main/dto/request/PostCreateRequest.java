package main.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
public class PostCreateRequest {
    private long timestamp;
    private boolean active;
    private List<String> tags;
    private String text;
    private String title;
}
