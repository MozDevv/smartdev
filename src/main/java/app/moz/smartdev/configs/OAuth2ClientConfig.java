package app.moz.smartdev.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public DefaultAuthorizationCodeTokenResponseClient tokenResponseClient() {
        return new DefaultAuthorizationCodeTokenResponseClient();
    }
}