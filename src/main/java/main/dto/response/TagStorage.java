package main.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TagStorage {

    private List<TagInfo> tags = new ArrayList<>();

    public void addTag(TagInfo tag){
        tags.add(tag);
    }
}
