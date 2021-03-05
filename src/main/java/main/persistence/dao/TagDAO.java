package main.persistence.dao;

import java.util.List;

import main.persistence.entity.Tag;
import main.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagDAO {

    private final TagRepository tagRepository;

    @Autowired
    public TagDAO(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getTags(){
        return (List<Tag>) tagRepository.findAll();
    }

    public Tag getTagByName(String name){
        return tagRepository.getByName(name).orElse(null);
    }

    public void saveTag(Tag tag){
        tagRepository.save(tag);
    }

}
