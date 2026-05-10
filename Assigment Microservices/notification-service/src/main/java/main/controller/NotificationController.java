package main.controller;

import main.dto.NotificationRequest;
import main.model.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<String> receiveNotification(@RequestBody NotificationRequest request) {
        new Thread(() -> notificationService.sendNotification(
                request.getEntity(),
                request.getName(),
                request.getType()
        )).start();

        return ResponseEntity.ok("Notification received");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("notification-service is running");
    }
}