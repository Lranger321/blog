package main.dto.responce;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
public class CalendarInfo {

    List<Integer> years;

    HashMap<String, Integer> posts;
    
}
