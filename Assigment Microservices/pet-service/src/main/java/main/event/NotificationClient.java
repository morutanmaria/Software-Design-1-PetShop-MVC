package main.event;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationClient implements EventListener {

    private final RestTemplate restTemplate;
    private static final String NOTIFICATION_SERVICE_URL = "http://localhost:8084/notifications";

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void update(ResourceEvent event) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("entity", event.getEntity());
            payload.put("name", event.getName());
            payload.put("type", event.getType());

            restTemplate.postForObject(NOTIFICATION_SERVICE_URL, payload, String.class);
        } catch (Exception e) {
            System.err.println("Could not reach notification-service: " + e.getMessage());
        }
    }
}
