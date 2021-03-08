package main.persistence.repository;

import main.persistence.entity.ModerationStatus;
import main.persistence.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Post findById(Long id);

    @Query("SELECT count(p) FROM Post p where p.text like concat('%',:search_text,'%')")
    long countByText(@Param("search_text") String Text);

    @Query("SELECT count(p) FROM Post p where p.moderationStatus='NEW' AND p.isActive=true")
    long countOfModeration();

    @Query("SELECT count(p) FROM Post p WHERE p.user.email = :email AND p.isActive=true " +
            "AND p.moderationStatus=:moderation_status")
    long countOfPostByUserAndModerationStatus(@Param("moderation_status") ModerationStatus status,
                                              @Param("email") String name);
    @Query("SELECT sum(p.viewCount) FROM Post p")
    long countViews();

    @Query("select min(p.time) FROM Post p")
    Date getFirstPost();

    @Query("select min(p.time) FROM Post p where p.user.email=:email")
    Date getFirstPost(@Param("email") String email);

    @Query("SELECT sum(p.viewCount) FROM Post p where p.user.email=:email")
    long countViews(@Param("email") String email);

    @Query("select count(p) from Post p WHERE p.user.email=:email")
    long countPost(@Param("email") String email);

}
