package main.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationClient implements EventListener {

    private final RestTemplate restTemplate;
    private final String notificationServiceUrl;

    public NotificationClient(RestTemplate restTemplate, @Value("${notification.service.url}") String notificationServiceUrl) {
        this.restTemplate = restTemplate;
        this.notificationServiceUrl = notificationServiceUrl;
    }

    @Override
    public void update(ResourceEvent event) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("entity", event.getEntity());
            payload.put("name", event.getName());
            payload.put("type", event.getType());

            restTemplate.postForObject(notificationServiceUrl + "/notifications", payload, String.class);
        } catch (Exception e) {
            System.err.println("Could not reach notification-service: " + e.getMessage());
        }
    }
}
