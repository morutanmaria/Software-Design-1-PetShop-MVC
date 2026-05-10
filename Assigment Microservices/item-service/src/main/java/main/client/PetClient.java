package main.client;

import main.dto.PetDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class PetClient {

    private final RestTemplate restTemplate;
    private final String petServiceUrl;

    public PetClient(RestTemplate restTemplate, @Value("${pet.service.url}") String petServiceUrl) {
        this.restTemplate = restTemplate;
        this.petServiceUrl = petServiceUrl;
    }

    public PetDTO getPetById(Integer id) {
        try {
            return restTemplate.getForObject(petServiceUrl + "/pets/" + id, PetDTO.class);
        } catch (Exception e) {
            System.err.println("Could not reach pet-service for pet id " + id + ": " + e.getMessage());
            return null;
        }
    }

    public List<PetDTO> getAllPets() {
        try {
            PetDTO[] pets = restTemplate.getForObject(petServiceUrl + "/pets", PetDTO[].class);
            return pets != null ? Arrays.asList(pets) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Could not reach pet-service: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public String getPetName(Integer petId) {
        if (petId == null) return "Unknown";
        PetDTO pet = getPetById(petId);
        return pet != null ? pet.getName() : "Unknown";
    }
}
