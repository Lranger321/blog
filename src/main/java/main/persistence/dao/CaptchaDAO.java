package main.persistence.dao;

import main.persistence.entity.CaptchaCode;
import main.persistence.repository.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaptchaDAO {

    @Autowired
    private CaptchaRepository captchaRepository;

    public void saveCaptcha(CaptchaCode captchaCode){
        captchaRepository.save(captchaCode);
    }

    public CaptchaCode getCaptcha(String captchaCode){
        return  captchaRepository.findBySecretCode(captchaCode).orElse(null);
    }

}
