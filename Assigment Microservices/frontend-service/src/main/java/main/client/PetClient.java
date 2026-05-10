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

    public PetClient(RestTemplate restTemplate,
                     @Value("${pet.service.url}") String petServiceUrl) {
        this.restTemplate = restTemplate;
        this.petServiceUrl = petServiceUrl;
    }

    public List<PetDTO> getAllPets(String search, String sortField, String sortDir) {
        try {
            StringBuilder url = new StringBuilder(petServiceUrl + "/pets?");
            if (search != null && !search.isBlank())
                url.append("search=").append(search).append("&");
            if (sortField != null && !sortField.isBlank())
                url.append("sortField=").append(sortField).append("&");
            if (sortDir != null)
                url.append("sortDir=").append(sortDir);

            PetDTO[] pets = restTemplate.getForObject(url.toString(), PetDTO[].class);
            return pets != null ? Arrays.asList(pets) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Could not reach pet-service: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public PetDTO getPetById(Integer id) {
        try {
            return restTemplate.getForObject(petServiceUrl + "/pets/" + id, PetDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void createPet(String name, String species, String breed,
                          Integer age, Double price) {
        try {
            java.util.Map<String, Object> body = new java.util.HashMap<>();
            body.put("name", name);
            body.put("species", species);
            body.put("breed", breed);
            body.put("age", age);
            body.put("price", price);
            body.put("available", true);
            restTemplate.postForObject(petServiceUrl + "/pets", body, PetDTO.class);
        } catch (Exception e) {
            System.err.println("Could not create pet: " + e.getMessage());
        }
    }

    public void updatePet(Integer id, String name, String species,
                          String breed, Integer age, Double price) {
        try {
            java.util.Map<String, Object> body = new java.util.HashMap<>();
            body.put("name", name);
            body.put("species", species);
            body.put("breed", breed);
            body.put("age", age);
            body.put("price", price);
            body.put("available", true);
            restTemplate.put(petServiceUrl + "/pets/" + id, body);
        } catch (Exception e) {
            System.err.println("Could not update pet: " + e.getMessage());
        }
    }

    public void deletePet(Integer id) {
        try {
            restTemplate.delete(petServiceUrl + "/pets/" + id);
        } catch (Exception e) {
            System.err.println("Could not delete pet: " + e.getMessage());
        }
    }
}
