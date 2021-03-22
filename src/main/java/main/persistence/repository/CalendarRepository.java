package main.persistence.repository;

import main.persistence.entity.PostCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<PostCalendar,Integer> {

    //todo to JPQL
    @Query(value = "SELECT YEAR(posts.time) FROM posts " +
            "where posts.is_active = true AND posts.moderation_status='ACCEPTED'" +
            "GROUP BY YEAR(posts.time)",nativeQuery = true)
    List<Integer> getYears();

    @Query(value = "SELECT posts.id, posts.time as date, count(posts.id) as count FROM posts " +
            "where posts.is_active = true AND posts.moderation_status='ACCEPTED' AND YEAR(posts.time) = :year " +
            "GROUP BY DATE(posts.time)",nativeQuery = true)
    List<PostCalendar> getCalendar(@Param("year") int year);

}
