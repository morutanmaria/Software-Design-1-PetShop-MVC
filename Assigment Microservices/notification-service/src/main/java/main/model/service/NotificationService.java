package main.model.service;

import main.event.EventListener;
import main.event.ResourceEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements EventListener {

    @Override
    public void update(ResourceEvent event) {
        sendNotification(event.getEntity(), event.getName(), event.getType());
    }

    public void sendNotification(String entity, String name, String type) {
        System.out.println("\n========== NOTIFICATION ==========");
        System.out.println("Entity : " + entity);
        System.out.println("Name   : " + name);
        System.out.println("Action : " + type);
        System.out.println("==================================\n");
    }
}