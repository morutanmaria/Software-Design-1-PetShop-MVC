package main.model.service;

import main.model.entity.Pet;

import java.util.List;
import java.util.Optional;

public interface PetService {
    Pet savePet(Pet pet);
    List<Pet> getAllPets();
    List<Pet> searchPets(String name);
    Optional<Pet> getPetById(Integer id);
    void deletePetById(Integer id);
    void updatePet(Pet pet);
    List<Pet> getAllPetsSorted(String sortField, String sortDir);
    List<Pet> searchAndSort(String name, String sortField, String sortDir);

}
