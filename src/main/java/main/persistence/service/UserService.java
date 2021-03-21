package main.persistence.service;

import main.dto.request.AuthRequest;
import main.dto.request.UserRequest;
import main.dto.response.AuthResponse;
import main.dto.response.RegisterDto;
import main.dto.response.StatisticsResponse;
import main.persistence.entity.CaptchaCode;
import main.persistence.entity.User;
import main.persistence.exceptions.ForbiddenException;
import main.persistence.exceptions.NotFoundException;
import main.persistence.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService {

    private final SMTP smtp;

    private final CaptchaRepository captchaRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final VotesRepository votesRepository;

    private final GlobalSettingsRepository settingsRepository;

    @Autowired
    public UserService(SMTP smtp, CaptchaRepository captchaRepository, AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder, UserRepository userRepository,
                       PostRepository postRepository, VotesRepository votesRepository,
                       GlobalSettingsRepository settingsRepository) {
        this.smtp = smtp;
        this.captchaRepository = captchaRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.votesRepository = votesRepository;
        this.settingsRepository = settingsRepository;
    }


    public AuthResponse checkAuth(Principal principal) {
        AuthResponse authResponse;
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).get();
            long moderationCount = (user.isModerator()) ? postRepository.countOfModeration() : 0;
            authResponse = Converter.createAuthResponse(true, user, moderationCount);
        } else {
            authResponse = Converter.createAuthResponse(false);
        }
        return authResponse;
    }

    public AuthResponse login(AuthRequest request) {
        System.out.println(passwordEncoder.encode(request.getPassword()));
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            User userInDB = userRepository.findByEmail(request.getEmail()).get();
            long moderationCount = (userInDB.isModerator()) ? postRepository.countOfModeration() : 0;
            return Converter.createAuthResponse(true, userInDB, moderationCount);
        } catch (BadCredentialsException ex) {
            return Converter.createAuthResponse(false);
        }
    }

    public RegisterDto userRegister(UserRequest request) {
        RegisterDto registerDto = new RegisterDto();
        if (settingsRepository.findByCode("MULTIUSER_MODE").get().getValue()) {
            HashMap<String, String> errors = registerErrors(request);
            if (errors.keySet().size() == 0) {
                User user = new User();
                user.setEmail(request.getEmail());
                user.setModerator(false);
                user.setName(request.getName());
                user.setRegTime(new Date());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setCode(null);
                userRepository.save(user);
                registerDto.setResult(true);
            } else {
                registerDto.setResult(false);
                registerDto.setErrors(errors);
            }
            return registerDto;
        }
        throw new NotFoundException();
    }

    private HashMap<String, String> registerErrors(UserRequest request) {
        HashMap<String, String> errors = new HashMap<>();
        if (!userRepository.findByEmail(request.getEmail()).isPresent()) {
            errors.put("e_mail", "Этот e-mail уже зарегистрирован");
        }
        if (request.getPassword().length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        Optional<CaptchaCode> captcha = captchaRepository.findBySecretCode(request.getCaptchaSecret());
        if (captcha.isPresent()) {
            String secretCode = captcha.get().getSecretCode();
            if (!secretCode.equals(request.getCaptcha())) {
                errors.put("captcha", "Код с картинки введён неверно");
            }
        }
        if (request.getName().length() < 3) {
            errors.put("name", "Имя указано неверно");
        }
        System.out.println(errors.keySet().size());
        return errors;
    }


    public StatisticsResponse getAllStat(String email) {
        if (!settingsRepository.findByCode("STATISTICS_IS_PUBLIC").get().getValue() &&
                !userRepository.findByEmail(email).get().isModerator()) {
            throw new ForbiddenException();
        }
        StatisticsResponse statisticsResponse = new StatisticsResponse();
        long countOfPost = postRepository.count();
        if (countOfPost > 0) {
            statisticsResponse.setPostsCount(postRepository.count());
            statisticsResponse.setViewsCount(postRepository.countViews());
            statisticsResponse.setLikesCount(votesRepository.countAllByValue(1));
            statisticsResponse.setDislikesCount(votesRepository.countAllByValue(-1));
            statisticsResponse.setFirstPublication(postRepository.getFirstPost().getTime() * 1000);
        } else {
            statisticsResponse.setPostsCount(0L);
            statisticsResponse.setViewsCount(0L);
            statisticsResponse.setLikesCount(0L);
            statisticsResponse.setDislikesCount(0L);
            statisticsResponse.setFirstPublication(null);
        }
        return statisticsResponse;
    }

    public StatisticsResponse getStatForUser(String email) {
        StatisticsResponse statisticsResponse = new StatisticsResponse();
        long countOfPost = postRepository.countPost(email);
        statisticsResponse.setPostsCount(countOfPost);
        if (countOfPost > 0) {
            statisticsResponse.setLikesCount(votesRepository.countVotes(1, email)
                    .stream().mapToLong(e -> e).sum());
            statisticsResponse.setDislikesCount(votesRepository.countVotes(-1, email)
                    .stream().mapToLong(e -> e).sum());
            statisticsResponse.setViewsCount(postRepository.countViews(email));
            statisticsResponse.setFirstPublication(postRepository.getFirstPost(email).getTime() * 1000);
        } else {
            statisticsResponse.setLikesCount(0L);
            statisticsResponse.setDislikesCount(0L);
            statisticsResponse.setViewsCount(0L);
            statisticsResponse.setFirstPublication(null);
        }
        return statisticsResponse;
    }


}
