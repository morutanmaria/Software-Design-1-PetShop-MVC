package main.controller;

import main.client.PetClient;
import main.dto.ItemDTO;
import main.dto.ItemRequest;
import main.dto.PetDTO;
import main.model.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final PetClient petClient;

    public ItemController(ItemService itemService, PetClient petClient) {
        this.itemService = itemService;
        this.petClient = petClient;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Integer id) {
        return itemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-pet/{petId}")
    public ResponseEntity<List<ItemDTO>> getItemsByPet(@PathVariable Integer petId) {
        return ResponseEntity.ok(itemService.findByPetId(petId));
    }

    @GetMapping("/pets")
    public ResponseEntity<List<PetDTO>> getAvailablePets() {
        return ResponseEntity.ok(petClient.getAllPets());
    }


    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemRequest request) {
        if (request.getPetId() != null && petClient.getPetById(request.getPetId()) == null) {
            return ResponseEntity.badRequest().build();
        }
        ItemDTO created = itemService.save(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Integer id,
                                               @RequestBody ItemRequest request) {
        try {
            ItemDTO updated = itemService.update(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        try {
            itemService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
