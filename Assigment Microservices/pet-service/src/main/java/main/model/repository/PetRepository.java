package main.model.repository;

import main.model.entity.Pet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    List<Pet> findAll(Sort sort);
    List<Pet> findPetByNameContainingIgnoreCase(String name);
}
