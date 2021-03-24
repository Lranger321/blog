package main.persistence.repository;

import main.persistence.entity.ModerationStatus;
import main.persistence.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PostPageRepository extends PagingAndSortingRepository<Post, Integer> {

    Page<Post> findAllByIsActiveAndModerationStatus
            (Boolean active, ModerationStatus moderation, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.isActive=true AND " +
            "p.moderationStatus='ACCEPTED' AND " +
            "p.isActive=true ORDER BY p.comments.size DESC ")
    Page<Post> sortedByComments(Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "JOIN fetch Vote v on v.post.id = p.id and v.value = 1 " +
            "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED'" +
            "GROUP BY p.id " +
            "ORDER BY COUNT(v) DESC"
    )
    Page<Post> sortedByLikes(Pageable pageable);

    @Query("FROM Post p where p.text like concat('%',:search_text,'%') and p.isActive=true")
    Page<Post> searchByText(Pageable page, @Param("search_text") String Text);

    @Query("FROM Post p where p.moderationStatus='NEW' order by p.time DESC")
    Page<Post> findAllNewForModeration(Pageable pageable);

    @Query("FROM Post p where p.moderationStatus=:moderation_status AND p.moderator.email =:moderator_email")
    Page<Post> findAllForModeration(Pageable pageable,
                                    @Param("moderation_status") ModerationStatus moderationStatus,
                                    @Param("moderator_email") String email);

    @Query("FROM Post p where p.user.email = :email AND p.isActive = false")
    Page<Post> findInactivePostByUserId(Pageable pageable, @Param("email") String email);

    @Query("FROM Post p WHERE p.user.email = :email AND p.isActive=true AND p.moderationStatus=:moderation_status")
    Page<Post> findPostsByUserId(Pageable pageable, @Param("email") String email,
                                 @Param("moderation_status") ModerationStatus moderationStatus);

    @Query("select p FROM Post p inner join p.tagList t where p.isActive=true AND t.name=:tag_name order by p.viewCount")
    Page<Post> findPostsByTag(Pageable pageable, @Param("tag_name") String tagName);

    @Query("FROM Post p" +
            " where p.isActive=true AND " +
            "p.moderationStatus='ACCEPTED' " +
            "AND p.time > :from_date AND " +
            "p.time < :to_date order by p.viewCount")
    Page<Post> findPostByDate(Pageable pageable, @Param("from_date") Date date, @Param("to_date") Date toDate);
}
