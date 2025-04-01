package app.moz.smartdev.configs;

import app.moz.smartdev.entity.AuthProvider;
import app.moz.smartdev.repository.AuthProviderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder {

    @Autowired
    private AuthProviderRepository authProviderRepository;

    @PostConstruct
    public void seedData() {
        List<AuthProvider> providers = Arrays.asList(
                new AuthProvider("GitHub",
                        "Ov23ctG6bWv9zmt4gdVu",
                        "8ab6a54a99fe8f6862110e4c35f5c528bb532d0a",
                        "https://github.com/login/oauth/authorize",
                        "https://github.com/login/oauth/access_token",
                        "http://localhost:8080/api/github/callback",
                        "read:user,repo",
                        "https://api.github.com/user",
                        "authorization_code",
                        true),
                new AuthProvider("Google",
                        "GoogleClientId",
                        "GoogleClientSecret",
                        "https://accounts.google.com/o/oauth2/auth",
                        "https://oauth2.googleapis.com/token",
                        "http://localhost:8080/api/google/callback",
                        "profile email",
                        "https://www.googleapis.com/oauth2/v3/userinfo",
                        "authorization_code",
                        true)
                // Add more providers as needed
        );

        authProviderRepository.saveAll(providers);
    }
}