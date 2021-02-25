package main.persistence.repository;

import main.persistence.entity.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CaptchaRepository extends JpaRepository<CaptchaCode,Integer> {

    Optional<CaptchaCode> findBySecretCode(String SecretCode);

}
