package main.persistence.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class SMTP {

    @Value("${mail.url}")
    private String url;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.fromMail}")
    private String email;

}
