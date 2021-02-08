package main.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CalendarInfo {

    List<Integer> years;

    HashMap<String, Integer> posts;

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public HashMap<String, Integer> getPosts() {
        return posts;
    }

    public void setPosts(HashMap<String, Integer> posts) {
        this.posts = posts;
    }

    
}
