package main.persistence.entity;

import com.google.gson.annotations.Expose;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_moderator",nullable = false)
    private boolean isModerator;

    @Column(name = "reg_time",nullable = false)
    private Date regTime;

    @Expose(serialize = false)
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String code;

    private String photo;

    public Role getRole(){
        return isModerator ? Role.MODERATOR : Role.USER;
    }

}
