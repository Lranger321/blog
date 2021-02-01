package main.persistence.repository;

import main.persistence.entity.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag,Integer> {
}
