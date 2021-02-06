package main.persistence.dao;

import java.util.List;

import main.persistence.entity.Tag;
import main.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagDAO {

    @Autowired
    TagRepository tagRepository;

    public List<Tag> getTags(){
        return (List<Tag>) tagRepository.findAll();
    }


}
