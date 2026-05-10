package main.client;

import main.dto.ItemDTO;
import main.dto.PetDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemClient {

    private final RestTemplate restTemplate;
    private final String itemServiceUrl;

    public ItemClient(RestTemplate restTemplate,
                      @Value("${item.service.url}") String itemServiceUrl) {
        this.restTemplate = restTemplate;
        this.itemServiceUrl = itemServiceUrl;
    }

    public List<ItemDTO> getAllItems() {
        try {
            ItemDTO[] items = restTemplate.getForObject(itemServiceUrl + "/items", ItemDTO[].class);
            return items != null ? Arrays.asList(items) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Could not reach item-service: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public ItemDTO getItemById(Integer id) {
        try {
            return restTemplate.getForObject(itemServiceUrl + "/items/" + id, ItemDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    public List<PetDTO> getAvailablePets() {
        try {
            PetDTO[] pets = restTemplate.getForObject(itemServiceUrl + "/items/pets", PetDTO[].class);
            return pets != null ? Arrays.asList(pets) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Could not get pets from item-service: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void createItem(String name, String description, double price,
                           String type, String imagePath, Integer petId) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("name", name);
            body.put("description", description);
            body.put("price", price);
            body.put("type", type);
            body.put("imagePath", imagePath);
            body.put("petId", petId);
            restTemplate.postForObject(itemServiceUrl + "/items", body, ItemDTO.class);
        } catch (Exception e) {
            System.err.println("Could not create item: " + e.getMessage());
        }
    }

    public void updateItem(Integer id, String name, String description, double price,
                           String type, String imagePath, Integer petId) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("name", name);
            body.put("description", description);
            body.put("price", price);
            body.put("type", type);
            body.put("imagePath", imagePath);
            body.put("petId", petId);
            restTemplate.put(itemServiceUrl + "/items/" + id, body);
        } catch (Exception e) {
            System.err.println("Could not update item: " + e.getMessage());
        }
    }

    public void deleteItem(Integer id) {
        try {
            restTemplate.delete(itemServiceUrl + "/items/" + id);
        } catch (Exception e) {
            System.err.println("Could not delete item: " + e.getMessage());
        }
    }
}
