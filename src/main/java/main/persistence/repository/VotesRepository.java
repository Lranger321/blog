package main.persistence.repository;

import main.persistence.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface VotesRepository extends JpaRepository<Vote, Integer> {

    long countAllByValue(int Value);

    @Query("SELECT count(vote) FROM Post p join p.votes vote WHERE vote.value=:value AND p.user.email =:email " +
            "group by p")
    List<Long> countVotes(@Param("value") int value, @Param("email") String email);

    @Query("FROM Vote v WHERE v.user.email=:email AND v.post.id=:post_id")
    Optional<Vote> findVoteByPostAndUser(@Param("email")String email,@Param("post_id") long postId);

}