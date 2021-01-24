package main.Entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "post_comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Comment parent;

    private Date time;

    private String text;


}
