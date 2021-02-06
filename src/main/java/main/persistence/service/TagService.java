package main.persistence.service;

import main.dto.TagInfo;
import main.dto.TagStorage;
import main.persistence.dao.PostDAO;
import main.persistence.dao.TagDAO;
import main.persistence.entity.Post;
import main.persistence.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TagService {

    @Autowired
    TagDAO tagDAO;

    @Autowired
    PostDAO postDAO;

    private double maxWeight = 0;

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


    private Double countTagWeight(String name) {
        List<Post> posts = postDAO.getPosts("recent");
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
        if (maxWeight == 0) {
            List<Post> posts = postDAO.getPosts("recent");
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
        }
        return maxWeight;
    }

}
