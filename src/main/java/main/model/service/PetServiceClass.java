package main.model.service;

import main.model.entity.Pet;
import main.model.repository.PetRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetServiceClass implements PetService{
    private final PetRepository petRepository;
    public PetServiceClass(PetRepository petRepository) {
        this.petRepository = petRepository;
    }
    @Override
    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
    }
    @Override
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }
    @Override
    public List<Pet> searchPets(String name) {
        if (name == null || name.isEmpty()) {
            return petRepository.findAll();
        }
        return petRepository.findPetByNameContainingIgnoreCase(name);
    }
    @Override
    public Optional<Pet> getPetById(Integer id){
        return petRepository.findById(id);
    }
    @Override
    public void deletePetById(Integer id){
        petRepository.deleteById(id);
    }
    @Override
    public void updatePet(Pet pet){
        petRepository.save(pet);
    }
    public List<Pet> getAllPetsSorted(String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        return petRepository.findAll(sort);
    }

    public List<Pet> searchAndSort(String name, String sortField, String sortDir) {
        List<Pet> pets = searchPets(name);
        pets.sort((p1, p2) -> {
            int result = 0;
            switch (sortField) {
                case "name": result = p1.getName().compareToIgnoreCase(p2.getName()); break;
                case "price": result = p1.getPrice().compareTo(p2.getPrice()); break;
                case "age": result = Integer.compare(p1.getAge(), p2.getAge()); break;
            }
            return sortDir.equalsIgnoreCase("asc") ? result : -result;
        });
        return pets;
    }
}
