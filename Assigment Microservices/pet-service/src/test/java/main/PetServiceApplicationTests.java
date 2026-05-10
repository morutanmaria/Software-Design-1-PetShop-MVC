package main;

import main.dto.PetDTO;
import main.dto.PetRequest;
import main.event.EventManager;
import main.model.entity.Pet;
import main.model.repository.PetRepository;
import main.model.service.PetServiceClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

	@Mock
	private PetRepository petRepository;

	@Mock
	private EventManager eventManager;

	@InjectMocks
	private PetServiceClass petService;

	private Pet samplePet;
	private PetRequest sampleRequest;

	@BeforeEach
	void setUp() {
		samplePet = new Pet("Buddy", "Dog", "Labrador", "Male",
				3, BigDecimal.valueOf(500), true, null);
		samplePet.setId(1);

		sampleRequest = new PetRequest();
		sampleRequest.setName("Buddy");
		sampleRequest.setSpecies("Dog");
		sampleRequest.setBreed("Labrador");
		sampleRequest.setGender("Male");
		sampleRequest.setAge(3);
		sampleRequest.setPrice(BigDecimal.valueOf(500));
		sampleRequest.setAvailable(true);
	}


	@Test
	void savePet_shouldSaveAndReturnDTO() {
		when(petRepository.save(any(Pet.class))).thenReturn(samplePet);

		PetDTO result = petService.savePet(sampleRequest);

		assertNotNull(result);
		assertEquals("Buddy", result.getName());
		assertEquals("Dog", result.getSpecies());
		verify(petRepository, times(1)).save(any(Pet.class));
		verify(eventManager, times(1)).notify(any());
	}

	@Test
	void updatePet_shouldUpdateFieldsAndReturnDTO() {
		when(petRepository.findById(1)).thenReturn(Optional.of(samplePet));
		when(petRepository.save(any(Pet.class))).thenReturn(samplePet);

		sampleRequest.setName("Max");
		PetDTO result = petService.updatePet(1, sampleRequest);

		assertNotNull(result);
		verify(petRepository).save(any(Pet.class));
		verify(eventManager).notify(any());
	}

	@Test
	void updatePet_shouldThrowWhenNotFound() {
		when(petRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> petService.updatePet(99, sampleRequest));
	}

	@Test
	void deletePet_shouldDeleteAndNotify() {
		when(petRepository.findById(1)).thenReturn(Optional.of(samplePet));

		petService.deletePetById(1);

		verify(petRepository).deleteById(1);
		verify(eventManager).notify(any());
	}

	@Test
	void deletePet_shouldThrowWhenNotFound() {
		when(petRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> petService.deletePetById(99));
	}


	@Test
	void getAllPets_shouldReturnListOfDTOs() {
		when(petRepository.findAll()).thenReturn(List.of(samplePet));

		List<PetDTO> result = petService.getAllPets();

		assertEquals(1, result.size());
		assertEquals("Buddy", result.get(0).getName());
	}

	@Test
	void getPetById_shouldReturnDTOWhenFound() {
		when(petRepository.findById(1)).thenReturn(Optional.of(samplePet));

		Optional<PetDTO> result = petService.getPetById(1);

		assertTrue(result.isPresent());
		assertEquals("Buddy", result.get().getName());
	}

	@Test
	void getPetById_shouldReturnEmptyWhenNotFound() {
		when(petRepository.findById(99)).thenReturn(Optional.empty());

		Optional<PetDTO> result = petService.getPetById(99);

		assertTrue(result.isEmpty());
	}

	@Test
	void searchPets_withNullName_shouldReturnAll() {
		when(petRepository.findAll()).thenReturn(List.of(samplePet));

		List<PetDTO> result = petService.searchPets(null);

		assertEquals(1, result.size());
	}

	@Test
	void searchPets_withName_shouldFilterResults() {
		when(petRepository.findPetByNameContainingIgnoreCase("Buddy"))
				.thenReturn(List.of(samplePet));

		List<PetDTO> result = petService.searchPets("Buddy");

		assertEquals(1, result.size());
		assertEquals("Buddy", result.get(0).getName());
	}
}
