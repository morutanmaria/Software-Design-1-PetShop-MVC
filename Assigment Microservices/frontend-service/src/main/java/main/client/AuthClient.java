package main.client;

import main.dto.LoggedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AuthClient {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthClient(RestTemplate restTemplate,
                      @Value("${auth.service.url}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    public LoggedUser login(String username, String password) {
        try {
            Map<String, String> body = Map.of(
                "username", username,
                "password", password
            );
            Map response = restTemplate.postForObject(
                authServiceUrl + "/auth/login", body, Map.class
            );
            if (response != null) {
                return new LoggedUser(
                    (String) response.get("username"),
                    (String) response.get("role"),
                    (String) response.get("token")
                );
            }
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
        }
        return null;
    }

    public boolean register(String username, String password, String email, String role) {
        try {
            Map<String, String> body = Map.of(
                "username", username,
                "password", password,
                "email", email,
                "role", role != null ? role : "SELLER"
            );
            restTemplate.postForObject(authServiceUrl + "/auth/register", body, String.class);
            return true;
        } catch (Exception e) {
            System.err.println("Register failed: " + e.getMessage());
            return false;
        }
    }
}
