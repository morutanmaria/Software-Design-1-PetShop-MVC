package main.model.service;

import main.dto.PetDTO;
import main.dto.PetRequest;
import main.event.EventManager;
import main.event.ResourceEvent;
import main.model.entity.Pet;
import main.model.repository.PetRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetServiceClass implements PetService {

    private final PetRepository petRepository;
    private final EventManager eventManager;

    public PetServiceClass(PetRepository petRepository, EventManager eventManager) {
        this.petRepository = petRepository;
        this.eventManager = eventManager;
    }


    @Override
    public PetDTO savePet(PetRequest request) {
        Pet pet = mapToPet(request);
        pet.setAvailable(true);
        Pet saved = petRepository.save(pet);
        eventManager.notify(new ResourceEvent("CREATED", "Pet", saved.getName(), saved));
        return PetDTO.from(saved);
    }

    @Override
    public PetDTO updatePet(Integer id, PetRequest request) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));

        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setGender(request.getGender());
        pet.setAge(request.getAge());
        pet.setPrice(request.getPrice());
        pet.setAvailable(request.isAvailable());
        pet.setImagePath(request.getImagePath());

        Pet saved = petRepository.save(pet);
        eventManager.notify(new ResourceEvent("UPDATED", "Pet", saved.getName(), saved));
        return PetDTO.from(saved);
    }

    @Override
    public void deletePetById(Integer id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));
        petRepository.deleteById(id);
        eventManager.notify(new ResourceEvent("DELETED", "Pet", pet.getName(), pet));
    }


    @Override
    public List<PetDTO> getAllPets() {
        return petRepository.findAll()
                .stream()
                .map(PetDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PetDTO> getPetById(Integer id) {
        return petRepository.findById(id).map(PetDTO::from);
    }

    @Override
    public Optional<Pet> getPetEntityById(Integer id) {
        return petRepository.findById(id);
    }

    @Override
    public List<PetDTO> searchPets(String name) {
        if (name == null || name.isEmpty()) {
            return petRepository.findAll()
                    .stream().map(PetDTO::from).collect(Collectors.toList());
        }
        return petRepository.findPetByNameContainingIgnoreCase(name)
                .stream().map(PetDTO::from).collect(Collectors.toList());
    }

    @Override
    public List<PetDTO> getAllPetsSorted(String sortField, String sortDir) {
        if (sortField == null || sortField.isBlank()) {
            return petRepository.findAll()
                    .stream().map(PetDTO::from).collect(Collectors.toList());
        }
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return petRepository.findAll(Sort.by(direction, sortField))
                .stream().map(PetDTO::from).collect(Collectors.toList());
    }

    @Override
    public List<PetDTO> searchAndSort(String name, String sortField, String sortDir) {
        List<PetDTO> pets = searchPets(name);

        if (sortField == null || sortField.isBlank()) {
            return pets;
        }

        pets.sort((p1, p2) -> {
            int result = switch (sortField) {
                case "name"  -> p1.getName().compareToIgnoreCase(p2.getName());
                case "price" -> p1.getPrice().compareTo(p2.getPrice());
                case "age"   -> Integer.compare(p1.getAge(), p2.getAge());
                default      -> 0;
            };
            return "desc".equalsIgnoreCase(sortDir) ? -result : result;
        });

        return pets;
    }


    private Pet mapToPet(PetRequest request) {
        Pet pet = new Pet();
        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setGender(request.getGender());
        pet.setAge(request.getAge());
        pet.setPrice(request.getPrice());
        pet.setAvailable(request.isAvailable());
        pet.setImagePath(request.getImagePath());
        return pet;
    }
}
