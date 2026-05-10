package main.config;

import main.event.EventManager;
import main.event.NotificationClient;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    private final EventManager eventManager;
    private final NotificationClient notificationClient;

    public EventConfig(EventManager eventManager, NotificationClient notificationClient) {
        this.eventManager = eventManager;
        this.notificationClient = notificationClient;
    }

    @PostConstruct
    public void init() {
        eventManager.subscribe(notificationClient);
    }
}
