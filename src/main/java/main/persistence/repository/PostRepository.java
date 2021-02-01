package main.persistence.repository;

import main.persistence.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT * FROM posts WHERE moderation_status = 1 AND is_active = 1",nativeQuery = true)
    List<Post> getAllFromCurrentDate();
}
