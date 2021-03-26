package main.persistence.repository;

import main.persistence.entity.Post;
import main.persistence.entity.PostCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Post,Integer> {

    @Query(value = "SELECT DISTINCT year(p.time) as year FROM Post p " +
            "where p.isActive = true AND p.moderationStatus='ACCEPTED'")
        List<Integer> getYears();

    @Query(value = "SELECT p.time as date, count(p.id) as count FROM Post p " +
            "where p.isActive = true AND p.moderationStatus='ACCEPTED' " +
            "AND p.time > :from_year AND p.time < :to_year  " +
            "GROUP BY p.time")
    List<PostCalendar> getCalendar(@Param("from_year") Date fromYear,@Param("to_year") Date toYear);

}
