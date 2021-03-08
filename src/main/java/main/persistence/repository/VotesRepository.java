package main.persistence.repository;

import main.persistence.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VotesRepository extends JpaRepository<Vote,Integer> {

    long countAllByValue(boolean Value);

    @Query("SELECT sum(p.votes.size) FROM Post p join fetch p.votes vote WHERE vote.value=:value AND p.user.email =:email")
    long countVotes(@Param("value") boolean value,@Param("email") String email);

}
