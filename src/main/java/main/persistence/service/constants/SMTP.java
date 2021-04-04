package main.persistence.service.constants;

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
