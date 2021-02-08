package main.persistence.repository;

import main.persistence.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT * FROM posts WHERE moderation_status = 'ACCEPTED' AND is_active = 1",nativeQuery = true)
    List<Post> getAllActiveAndAccepted();

    @Query(value = "SELECT * FROM posts WHERE moderation_status = 'ACCEPTED' AND is_active = 1 ORDER BY time ASC "
            ,nativeQuery = true)
    List<Post> getAllSortedByDate();

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
    List<Post> getAllSortedByDateAsc();

}
