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

    public List<PetDTO> getAllPets() {
        try {
            PetDTO[] pets = restTemplate.getForObject(petServiceUrl + "/pets", PetDTO[].class);
            return pets != null ? Arrays.asList(pets) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Could not reach pet-service: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<PetDTO> searchAndSort(String search, String sortField, String sortDir) {
        try {
            StringBuilder url = new StringBuilder(petServiceUrl + "/pets?");
            if (search != null)    url.append("search=").append(search).append("&");
            if (sortField != null) url.append("sortField=").append(sortField).append("&");
            if (sortDir != null)   url.append("sortDir=").append(sortDir);

            PetDTO[] pets = restTemplate.getForObject(url.toString(), PetDTO[].class);
            return pets != null ? Arrays.asList(pets) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Could not reach pet-service: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
