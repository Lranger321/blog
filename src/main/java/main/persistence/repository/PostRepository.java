package main.persistence.repository;

import main.persistence.entity.ModerationStatus;
import main.persistence.entity.Post;
import main.persistence.entity.PostCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findById(long Id);

    @Query("SELECT count(p) FROM Post p where p.moderationStatus='NEW' AND p.isActive=true")
    long countOfModeration();

    @Query("SELECT sum(p.viewCount) FROM Post p")
    long countViews();

    @Query("select min(p.time) FROM Post p")
    Date getFirstPost();

    //todo to JPQL
    @Query(value = "SELECT posts.time, count(posts.id) FROM posts " +
            "where posts.is_active = true AND posts.moderation_status='ACCEPTED'" +
            "GROUP BY DATE(posts.time)",nativeQuery = true)
    PostCalendar getCalendar();

    //@Query()
   // PostCalendar getCalendarByYear(Date year);

    @Query("select min(p.time) FROM Post p where p.user.email=:email")
    Date getFirstPost(@Param("email") String email);

    @Query("SELECT sum(p.viewCount) FROM Post p where p.user.email=:email")
    long countViews(@Param("email") String email);

    @Query("select count(p) from Post p WHERE p.user.email=:email")
    long countPost(@Param("email") String email);

}
