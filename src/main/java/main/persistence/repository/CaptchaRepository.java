package main.persistence.repository;

import main.persistence.entity.CaptchaCode;
import org.springframework.data.repository.CrudRepository;

public interface CaptchaRepository extends CrudRepository<CaptchaCode,Integer> {
}
