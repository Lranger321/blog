package main.persistence.entity;

import lombok.*;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "post_comments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    private Comment parent;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
