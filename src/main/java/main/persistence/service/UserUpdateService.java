package main.persistence.service;

import com.github.cage.GCage;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.dto.request.ChangePasswordRequest;
import main.dto.request.PasswordRestoreRequest;
import main.dto.request.UserDeletePhotoRequest;
import main.dto.request.UserUpdateRequest;
import main.dto.response.ChangePasswordResponse;
import main.dto.response.PasswordRestoreResponse;
import main.dto.response.UserUpdateResponse;
import main.persistence.entity.User;
import main.persistence.repository.CaptchaRepository;
import main.persistence.repository.UserRepository;
import main.persistence.service.constants.SMTP;
import org.apache.http.entity.ContentType;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;

@Service
public class UserUpdateService {

    private final UserRepository userRepository;
    private final SMTP smtp;
    private final PasswordEncoder passwordEncoder;
    private final CaptchaRepository captchaRepository;
    private final ImageService imageService;

    @Autowired
    public UserUpdateService(UserRepository userRepository, SMTP smtp,
                             PasswordEncoder passwordEncoder, CaptchaRepository captchaRepository, ImageService imageService) {
        this.userRepository = userRepository;
        this.smtp = smtp;
        this.passwordEncoder = passwordEncoder;
        this.captchaRepository = captchaRepository;
        this.imageService = imageService;
    }


    public PasswordRestoreResponse passwordRestore(PasswordRestoreRequest restoreRequest) {
        String email = restoreRequest.getEmail();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            String token = DigestUtils.md5DigestAsHex(new GCage().getTokenGenerator().next().getBytes());
            String url = "127.0.0.1:8080/login/change-password/" + token;
            try {
                Unirest.post(smtp.getUrl() + "/messages")
                        .basicAuth("api", smtp.getPassword())
                        .queryString("from", smtp.getEmail())
                        .queryString("to", email)
                        .queryString("subject", "PubBlog restore password")
                        .queryString("text", url)
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
                return null;
            }
            user.setCode(token);
            userRepository.save(user);
            return new PasswordRestoreResponse(true);
        } else {
            return new PasswordRestoreResponse(false);
        }
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request, String email) {
        User user = userRepository.findByEmail(email).get();
        HashMap<String, String> errors = changePasswordErrors(request, user);
        if (!errors.keySet().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            return new ChangePasswordResponse(true, null);
        } else {
            return new ChangePasswordResponse(false, errors);
        }
    }

    private HashMap<String, String> changePasswordErrors(ChangePasswordRequest request, User user) {
        HashMap<String, String> errors = new HashMap<>();
        if (user.getCode().equals(request.getCode())) {
            errors.put("code", "Ссылка для восстановления пароля устарела." +
                    "<a href=/auth/restore\">Запросить ссылку снова</a>");
        }
        if (request.getPassword().length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (!captchaRepository.findBySecretCode(request.getSecretCode()).get().getCode().equals(request.getCaptcha())) {
            errors.put("captcha", "Код с картинки введён неверно");
        }
        return errors;
    }

    public UserUpdateResponse updateUser(UserDeletePhotoRequest request, String email) {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail(request.getEmail());
        userUpdateRequest.setName(request.getName());
        userUpdateRequest.setRemovePhoto(request.getRemovePhoto());
        userUpdateRequest.setPassword(request.getPassword());
        userUpdateRequest.setPhoto(null);
        return updateUser(userUpdateRequest, email);
    }


    public UserUpdateResponse updateUser(UserUpdateRequest request, String email) {
        HashMap<String, String> errors = updateUserErrors(request, email);
        if (errors.isEmpty()) {
            User user = userRepository.findByEmail(email).get();
            user.setName(request.getName());
            if (!email.equals(request.getEmail())) {
                user.setEmail(request.getEmail());
            }
            if (request.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            if (request.getRemovePhoto() != null) {
                if (request.getRemovePhoto() == 0) {
                    try {
                        uploadPhoto(request, user);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                } else {
                    System.out.println("Photo: "+user.getPhoto().split("/",3)[2]);
                    imageService.deleteFileFromBucket(user.getPhoto().split("/",2)[1]);
                    user.setPhoto("");
                }
            }
            userRepository.save(user);
            return new UserUpdateResponse(true, null);
        } else {
            return new UserUpdateResponse(false, errors);
        }
    }

    private HashMap<String, String> updateUserErrors(UserUpdateRequest request, String email) {
        HashMap<String, String> errors = new HashMap<>();
        if (request.getName().length() < 6) {
            errors.put("name", "Имя указано неверно");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent() && !email.equals(request.getEmail())) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (request.getPassword() != null) {
            if (request.getPassword().length() < 6) {
                errors.put("password", "Пароль короче 6-ти символов");
            }
        }

        return errors;
    }

    private void uploadPhoto(UserUpdateRequest request, User user) throws IOException {
        BufferedImage image = ImageIO.read(request.getPhoto().getInputStream());
        image = Scalr.resize(image, 36, 36);
        String[] typeSplit = request.getPhoto().getOriginalFilename().split("\\.");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, typeSplit[typeSplit.length - 1], baos );
        baos.flush();
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        MultipartFile file = new MockMultipartFile(
                request.getPhoto().getName(),request.getPhoto().getOriginalFilename(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
        String path = imageService.uploadFile(file);
        user.setPhoto(path);
    }


}
