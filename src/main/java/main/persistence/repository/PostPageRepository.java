package main.persistence.repository;

import main.persistence.entity.ModerationStatus;
import main.persistence.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostPageRepository extends PagingAndSortingRepository<Post, Integer> {

    List<Post> findAllByIsActiveAndModerationStatus
            (Boolean active, ModerationStatus moderation, Pageable pageable);

    @Query("FROM Post p WHERE p.isActive=true AND p.moderationStatus='ACCEPTED' ORDER BY p.comments.size DESC ")
    List<Post> sortedByComments(Pageable pageable);

    @Query("FROM Post p where p.isActive=true AND p.moderationStatus='ACCEPTED' order by p.votes.size desc")
    List<Post> sortedByLikes(Pageable pageable);

    //List<Post> findAllByIsActiveAndModerationStatusOrderByVotes();

    @Query("FROM Post p where p.text like concat('%',:search_text,'%')")
    List<Post> searchByText(Pageable page, @Param("search_text") String Text);

    @Query("FROM Post p where p.moderationStatus='NEW' order by p.time DESC")
    List<Post> findAllNewForModeration(Pageable pageable);

    @Query("FROM Post p where p.moderationStatus=:moderation_status AND p.moderator.email =:moderator_email")
    List<Post> findAllForModeration(Pageable pageable,
                                    @Param("moderation_status") ModerationStatus moderationStatus,
                                    @Param("moderator_email") String email);

    @Query("FROM Post p where p.user.email = :email AND p.isActive = false")
    List<Post> findInactivePostByUserId(Pageable pageable,@Param("email") String email);

    @Query("FROM Post p WHERE p.user.email = :email AND p.isActive=true AND p.moderationStatus=:moderation_status")
    List<Post> findPostsByUserId(Pageable pageable,@Param("email")String email,
                                 @Param("moderation_status") ModerationStatus moderationStatus);
}
