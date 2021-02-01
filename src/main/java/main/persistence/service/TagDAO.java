package main.persistence.service;

import main.dto.TagInfo;
import main.dto.TagStorage;
import main.persistence.repository.PostRepository;
import main.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagDAO {

    @Autowired
    TagRepository tagRepository;
    @Autowired
    PostRepository postRepository;

    public TagStorage countAllWeight() {
        TagStorage tagStorage = new TagStorage();
        tagRepository.findAll().forEach(tag -> {
            TagInfo tagInfo = new TagInfo();
            tagInfo.setName(tag.getName());
            tagInfo.setWeight(countWeight(tag.getName()));
            tagStorage.addTag(tagInfo);
        });
        return tagStorage;
    }

    /*
    TODO Counting
     */
    public double countWeight(String name) {
        long countOfPost = postRepository.findAll().stream().filter(e->e.getTagList().contains(name)).count();
        //Рассчет веса
        double dWeight = 0;
        return 0;
    }


}
