package main.persistence.service;

import com.github.cage.GCage;
import main.dto.CaptchaResponse;
import main.persistence.dao.CaptchaDAO;
import main.persistence.entity.CaptchaCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@Service
public class CaptchaService {

    @Autowired
    private CaptchaDAO captchaDAO;

    public CaptchaResponse createCaptcha() {
        CaptchaResponse captchaResponse = new CaptchaResponse();
        GCage gCage = new GCage();
        String token = gCage.getTokenGenerator().next();
        String secretCode = gCage.getTokenGenerator().next();
        String image = Base64.getEncoder().encodeToString(gCage.draw(token));
        System.out.println(secretCode+":"+token);
        saveCaptcha(token, secretCode);
        image = "data:image/png;base64," + image;
        captchaResponse.setImage(image);
        captchaResponse.setSecret(secretCode);
        return captchaResponse;
    }

    private void saveCaptcha(String code, String secretCode) {
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCode(code);
        captchaCode.setSecretCode(secretCode);
        captchaCode.setTime(new Date());
        captchaDAO.saveCaptcha(captchaCode);
    }

}
