package main.client;

import main.dto.ItemDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
}
