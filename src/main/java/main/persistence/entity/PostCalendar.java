package main.persistence.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class PostCalendar  {

    @Id
    private int id;

    private Date date;

    private long count;

}
