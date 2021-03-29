package main.persistence.repository;

import main.persistence.entity.ModerationStatus;
import main.persistence.entity.Post;
import main.persistence.entity.PostCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.*;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findById(long Id);

    @Query("SELECT count(p) FROM Post p where p.moderationStatus='NEW' AND p.isActive=true")
    long countOfModeration();

    @Query("SELECT sum(p.viewCount) FROM Post p")
    long countViews();

    List<Post> findAllByIsActiveAndModerationStatus(boolean Active,ModerationStatus moderationStatus);

    Post findFirstByOrderByTimeAsc();

    @Query("select MIN(p.time) FROM Post p where p.user.email=:email")
    Date getFirstPost(@Param("email") String email);

    @Query("SELECT sum(p.viewCount) FROM Post p where p.user.email=:email")
    long countViews(@Param("email") String email);

    @Query("select count(p) from Post p WHERE p.user.email=:email")
    long countPost(@Param("email") String email);

}
