package main.model.service;

import main.dto.PetDTO;
import main.dto.PetRequest;
import main.model.entity.Pet;

import java.util.List;
import java.util.Optional;

public interface PetService {

    PetDTO savePet(PetRequest request);
    PetDTO updatePet(Integer id, PetRequest request);
    void deletePetById(Integer id);

    List<PetDTO> getAllPets();
    Optional<PetDTO> getPetById(Integer id);
    List<PetDTO> searchPets(String name);
    List<PetDTO> getAllPetsSorted(String sortField, String sortDir);
    List<PetDTO> searchAndSort(String name, String sortField, String sortDir);

    Optional<Pet> getPetEntityById(Integer id);
}
