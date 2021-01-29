package main.Database;

import main.Entity.Post;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagDAO {

    @Autowired
    TagRepository tagRepository;
    @Autowired
    PostRepository postRepository;

    public String countAllWeight() {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        tagRepository.findAll().forEach(tag -> {
            jsonArray.add(countWeight(tag.getName()));
        });
        json.put("tags",jsonArray);
        return json.toJSONString();
    }


    public JSONObject countWeight(String name) {
        JSONObject json = new JSONObject();
        json.put("name",name);

        long countOfPost = postRepository.findAll().stream().filter(e->e.getTagList().contains(name)).count();
        //Рассчет веса
        double dWeight = 0;
        return json;
    }


}
