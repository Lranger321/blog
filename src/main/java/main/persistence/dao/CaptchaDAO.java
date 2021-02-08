package main.persistence.dao;

import main.persistence.entity.CaptchaCode;
import main.persistence.repository.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaptchaDAO {

    @Autowired
    CaptchaRepository captchaRepository;

    public void saveCaptcha(CaptchaCode captchaCode){
        captchaRepository.save(captchaCode);
    }

}
