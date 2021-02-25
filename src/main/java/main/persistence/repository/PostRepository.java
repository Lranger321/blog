package main.persistence.repository;

import main.persistence.entity.ModerationStatus;
import main.persistence.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

   /* @Query(value = "SELECT Post FROM Post WHERE Post.moderationStatus = 'ACCEPTED' AND Post.isActive = true ")
    List<Post> getAllActiveAndAccepted();

    @Query(value = "SELECT Post FROM Post WHERE  Post.moderationStatus = 'ACCEPTED'  AND Post.isActive = true" +
            " ORDER BY time ASC ")
    List<Post> getAllSortedByDate(Pageable pageable);

    @Query(value = "SELECT *,(SELECT COUNT(*) FROM posts_votes WHERE value = -1 AND post.id = post_id) as like_count " +
            "FROM posts as post WHERE moderation_status = 'ACCEPTED' AND is_active = 1 ORDER BY like_count DESC "
            ,nativeQuery = true)
    List<Post> getAllSortedByLikes();

    @Query(value = "SELECT *,(SELECT COUNT(*) FROM post_comments WHERE  post.id = post_id) as comment_count " +
            "FROM posts as post WHERE moderation_status = 'ACCEPTED' AND is_active = 1 ORDER BY comment_count DESC "
            ,nativeQuery = true)
    List<Post> getAllSortedByComments();

    @Query(value = "SELECT * FROM posts WHERE moderation_status = 'ACCEPTED' AND is_active = 1 ORDER BY time DESC "
            ,nativeQuery = true)
    List<Post> getAllSortedByDateAsc();*/

    Post findById(int id);

    @Query("SELECT count(p) FROM Post p where p.text like concat('%',:search_text,'%')")
    long countByText(@Param("search_text") String Text);

}
