package main.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts_votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    private Date time;

    private String value;

}
