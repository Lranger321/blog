package main.persistence.service;

import com.github.cage.GCage;
import main.dto.response.CaptchaResponse;
import main.persistence.entity.CaptchaCode;
import main.persistence.repository.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class CaptchaService {

    private final CaptchaRepository captchaRepository;

    @Autowired
    public CaptchaService(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }


    public CaptchaResponse createCaptcha() {
        CaptchaResponse captchaResponse = new CaptchaResponse();
        GCage gCage = new GCage();
        String token = gCage.getTokenGenerator().next();
        String secretCode = gCage.getTokenGenerator().next();
        String image = Base64.getEncoder().encodeToString(gCage.draw(token));
        saveCaptcha(token, secretCode);
        image = "data:image/png;base64," + image;
        captchaResponse.setImage(image);
        captchaResponse.setSecret(secretCode);
        return captchaResponse;
    }

    private void saveCaptcha(String code, String secretCode) {
        CaptchaCode captchaCode = CaptchaCode.builder()
                .code(code)
                .secretCode(secretCode)
                .time(new Date()).build();
        captchaRepository.save(captchaCode);
    }

}
