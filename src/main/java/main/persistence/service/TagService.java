package main.persistence.service;

import main.dto.response.*;
import main.dto.response.TagStorage;
import main.persistence.entity.Post;
import main.persistence.entity.Tag;
import main.persistence.repository.PostRepository;
import main.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    private final PostRepository postRepository;

    @Autowired
    public TagService(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }


    public TagStorage countAllWeight() {
        TagStorage tagStorage = new TagStorage();
        tagRepository.findAll().forEach(tag -> {
            TagInfo tagInfo = new TagInfo();
            tagInfo.setName(tag.getName());
            tagInfo.setWeight(countTagWeight(tag.getName()));
            tagStorage.addTag(tagInfo);
        });
        return tagStorage;
    }


    private Double countTagWeight(String name) {
        List<Post> posts = postRepository.findAll();
        long countOfPosts = posts.size();
        long countOfPostsWithTag = posts.stream()
                .filter(post -> {
                    boolean containsTag = false;
                    for (Tag postTag : post.getTagList()) {
                        if (postTag.getName().equals(name)) {
                            containsTag = true;
                            break;
                        }

                    }
                    return containsTag;
                }).count();
        //Рассчет веса
        double dWeight = countOfPostsWithTag / (double) countOfPosts;
        double maxWeight = findMaxWeight();
        double k = 1 / maxWeight;
        double weightOfTag = k * dWeight;

        return weightOfTag;
    }

    private double findMaxWeight() {
        double maxWeight = 0;
        List<Post> posts = postRepository.findAll();
        long countOfPosts = posts.size();
        HashMap<String, Integer> count = new HashMap<>();
        tagRepository.findAll().forEach(tag -> {
            count.put(tag.getName(), (int)
                    posts.stream().filter(post -> {
                        boolean containsTag = false;
                        for (Tag postTag : post.getTagList()) {
                            if (postTag.getName().equals(tag.getName())) {
                                containsTag = true;
                                break;
                            }

                        }
                        return containsTag;
                    }).count());
        });
        int max = 0;
        for (String name : count.keySet()) {
            if (max < count.get(name)) {
                max = count.get(name);
            }
        }
        maxWeight = max / (double) countOfPosts;
        return maxWeight;
    }

}
