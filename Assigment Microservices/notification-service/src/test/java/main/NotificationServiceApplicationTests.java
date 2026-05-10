package main;

import main.model.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class NotificationServiceApplicationTests {

	private final NotificationService notificationService = new NotificationService();


	@Test
	void sendNotification_created_shouldNotThrow() {
		assertDoesNotThrow(() ->
				notificationService.sendNotification("Pet", "Buddy", "CREATED")
		);
	}

	@Test
	void sendNotification_updated_shouldNotThrow() {
		assertDoesNotThrow(() ->
				notificationService.sendNotification("Item", "Dog Food", "UPDATED")
		);
	}

	@Test
	void sendNotification_deleted_shouldNotThrow() {
		assertDoesNotThrow(() ->
				notificationService.sendNotification("Pet", "Buddy", "DELETED")
		);
	}


	@Test
	void update_withResourceEvent_shouldNotThrow() {
		main.event.ResourceEvent event =
				new main.event.ResourceEvent("CREATED", "Pet", "Buddy", null);

		assertDoesNotThrow(() -> notificationService.update(event));
	}
}