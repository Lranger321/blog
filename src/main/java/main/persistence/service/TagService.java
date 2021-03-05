package main.persistence.service;

import main.dto.response.*;
import main.dto.response.TagStorage;
import main.persistence.dao.PostDAO;
import main.persistence.dao.TagDAO;
import main.persistence.entity.Post;
import main.persistence.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TagService {

    private final TagDAO tagDAO;

    private final PostDAO postDAO;

    public TagService(TagDAO tagDAO, PostDAO postDAO) {
        this.tagDAO = tagDAO;
        this.postDAO = postDAO;
    }


    public TagStorage countAllWeight() {
        TagStorage tagStorage = new TagStorage();
        tagDAO.getTags().forEach(tag -> {
            TagInfo tagInfo = new TagInfo();
            tagInfo.setName(tag.getName());
            tagInfo.setWeight(countTagWeight(tag.getName()));
            tagStorage.addTag(tagInfo);
        });
        return tagStorage;
    }

    public List<Tag> createTagList(List<String> tags) {
        List<Tag> tagList = new ArrayList<>();
        for (String name : tags) {
            Tag tag = tagDAO.getTagByName(name);
            if (tag == null) {
                tag = new Tag();
                tag.setName(name);
                tagDAO.saveTag(tag);
                tag = tagDAO.getTagByName(name);
            }
            tagList.add(tag);
        }
        return tagList;
    }


    private Double countTagWeight(String name) {
        List<Post> posts = postDAO.getPosts("recent", 0, postDAO.getPostCount());
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
        List<Post> posts = postDAO.getPosts("recent", 0, postDAO.getPostCount());
        long countOfPosts = posts.size();
        HashMap<String, Integer> count = new HashMap<>();
        tagDAO.getTags().forEach(tag -> {
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
