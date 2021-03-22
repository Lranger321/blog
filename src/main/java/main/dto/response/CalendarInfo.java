package main.dto.response;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class CalendarInfo {

    List<Integer> years;

    HashMap<String, Long> posts;
    
}
