package main.persistence.repository;

import main.persistence.entity.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostForRequestRepository extends JpaRepository<PostView, Integer> {
    @Query(value = "SELECT * FROM post_for_request ORDER BY timestamp", nativeQuery = true)
    List<PostView> getAllRecent();

    @Query(value = "SELECT * FROM post_for_request ORDER BY commentCount", nativeQuery = true)
    List<PostView> getAllPopular();

    @Query(value = "SELECT * FROM post_for_request ORDER BY likeCount", nativeQuery = true)
    List<PostView> getAllBest();

    @Query(value = "SELECT * FROM post_for_request ORDER BY timestamp DESC", nativeQuery = true)
    List<PostView> getAllEarly();
}
