package main.persistence.repository;

import main.persistence.entity.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSetting,Integer> {

    Optional<GlobalSetting> findByCode(String Code);

}
