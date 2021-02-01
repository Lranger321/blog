package main.dto;

import java.util.List;

public class TagStorage {

    private List<TagInfo> tags;

    public void addTag(TagInfo tag){
        tags.add(tag);
    }

    public List<TagInfo> getTags() {
        return tags;
    }

    public void setTags(List<TagInfo> tags) {
        this.tags = tags;
    }
}
