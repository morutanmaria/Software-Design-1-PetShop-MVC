package main;

import main.client.PetClient;
import main.dto.ItemDTO;
import main.dto.ItemRequest;
import main.event.EventManager;
import main.model.entity.Item;
import main.model.entity.ItemType;
import main.model.repository.ItemRepository;
import main.model.service.ItemServiceClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceApplicationTests {

	@Mock
	private ItemRepository itemRepository;

	@Mock
	private EventManager eventManager;

	@Mock
	private PetClient petClient;

	@InjectMocks
	private ItemServiceClass itemService;

	private Item sampleItem;
	private ItemRequest sampleRequest;

	@BeforeEach
	void setUp() {
		sampleItem = new Item("Dog Food", "Premium food", 25.99, ItemType.FOOD, null, 1);
		sampleItem.setId(1);

		sampleRequest = new ItemRequest();
		sampleRequest.setName("Dog Food");
		sampleRequest.setDescription("Premium food");
		sampleRequest.setPrice(25.99);
		sampleRequest.setType(ItemType.FOOD);
		sampleRequest.setPetId(1);
	}


	@Test
	void save_shouldSaveItemAndReturnDTO() {
		when(itemRepository.save(any(Item.class))).thenReturn(sampleItem);
		when(petClient.getPetName(1)).thenReturn("Buddy");

		ItemDTO result = itemService.save(sampleRequest);

		assertNotNull(result);
		assertEquals("Dog Food", result.getName());
		assertEquals("Buddy", result.getPetName());
		verify(itemRepository).save(any(Item.class));
		verify(eventManager).notify(any());
	}

	@Test
	void update_shouldUpdateFieldsAndReturnDTO() {
		when(itemRepository.findById(1)).thenReturn(Optional.of(sampleItem));
		when(itemRepository.save(any(Item.class))).thenReturn(sampleItem);
		when(petClient.getPetName(1)).thenReturn("Buddy");

		sampleRequest.setName("Cat Food");
		ItemDTO result = itemService.update(1, sampleRequest);

		assertNotNull(result);
		verify(itemRepository).save(any(Item.class));
		verify(eventManager).notify(any());
	}

	@Test
	void update_shouldThrowWhenItemNotFound() {
		when(itemRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> itemService.update(99, sampleRequest));
	}

	@Test
	void deleteById_shouldDeleteAndNotify() {
		when(itemRepository.findById(1)).thenReturn(Optional.of(sampleItem));

		itemService.deleteById(1);

		verify(itemRepository).deleteById(1);
		verify(eventManager).notify(any());
	}

	@Test
	void deleteById_shouldThrowWhenNotFound() {
		when(itemRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> itemService.deleteById(99));
	}


	@Test
	void findAll_shouldReturnListOfDTOs() {
		when(itemRepository.findAll()).thenReturn(List.of(sampleItem));
		when(petClient.getPetName(1)).thenReturn("Buddy");

		List<ItemDTO> result = itemService.findAll();

		assertEquals(1, result.size());
		assertEquals("Dog Food", result.get(0).getName());
		assertEquals("Buddy", result.get(0).getPetName());
	}

	@Test
	void findById_shouldReturnDTOWhenFound() {
		when(itemRepository.findById(1)).thenReturn(Optional.of(sampleItem));
		when(petClient.getPetName(1)).thenReturn("Buddy");

		Optional<ItemDTO> result = itemService.findById(1);

		assertTrue(result.isPresent());
		assertEquals("Dog Food", result.get().getName());
	}

	@Test
	void findById_shouldReturnEmptyWhenNotFound() {
		when(itemRepository.findById(99)).thenReturn(Optional.empty());

		Optional<ItemDTO> result = itemService.findById(99);

		assertTrue(result.isEmpty());
	}

	@Test
	void findByPetId_shouldReturnItemsForPet() {
		when(itemRepository.findByPetId(1)).thenReturn(List.of(sampleItem));
		when(petClient.getPetName(1)).thenReturn("Buddy");

		List<ItemDTO> result = itemService.findByPetId(1);

		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getPetId());
	}
}
