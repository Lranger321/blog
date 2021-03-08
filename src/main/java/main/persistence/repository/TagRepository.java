package main.persistence.repository;

import main.persistence.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TagRepository extends CrudRepository<Tag,Integer> {

    Optional<Tag> findByName(String Name);

}
