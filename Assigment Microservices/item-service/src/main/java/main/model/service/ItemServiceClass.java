package main.model.service;

import main.client.PetClient;
import main.dto.ItemDTO;
import main.dto.ItemRequest;
import main.event.EventManager;
import main.event.ResourceEvent;
import main.model.entity.Item;
import main.model.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceClass implements ItemService {

    private final ItemRepository itemRepository;
    private final EventManager eventManager;
    private final PetClient petClient;

    public ItemServiceClass(ItemRepository itemRepository,
                            EventManager eventManager,
                            PetClient petClient) {
        this.itemRepository = itemRepository;
        this.eventManager = eventManager;
        this.petClient = petClient;
    }


    @Override
    public ItemDTO save(ItemRequest request) {
        Item item = mapToItem(request);
        Item saved = itemRepository.save(item);
        eventManager.notify(new ResourceEvent("CREATED", "Item", saved.getName(), saved));
        String petName = petClient.getPetName(saved.getPetId());
        return ItemDTO.from(saved, petName);
    }

    @Override
    public ItemDTO update(Integer id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setType(request.getType());
        item.setImagePath(request.getImagePath());
        item.setPetId(request.getPetId());

        Item saved = itemRepository.save(item);
        eventManager.notify(new ResourceEvent("UPDATED", "Item", saved.getName(), saved));
        String petName = petClient.getPetName(saved.getPetId());
        return ItemDTO.from(saved, petName);
    }

    @Override
    public void deleteById(Integer id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        itemRepository.deleteById(id);
        eventManager.notify(new ResourceEvent("DELETED", "Item", item.getName(), item));
    }


    @Override
    public List<ItemDTO> findAll() {
        return itemRepository.findAll()
                .stream()
                .map(item -> ItemDTO.from(item, petClient.getPetName(item.getPetId())))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ItemDTO> findById(Integer id) {
        return itemRepository.findById(id)
                .map(item -> ItemDTO.from(item, petClient.getPetName(item.getPetId())));
    }

    @Override
    public List<ItemDTO> findByPetId(Integer petId) {
        return itemRepository.findByPetId(petId)
                .stream()
                .map(item -> ItemDTO.from(item, petClient.getPetName(item.getPetId())))
                .collect(Collectors.toList());
    }

    private Item mapToItem(ItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setType(request.getType());
        item.setImagePath(request.getImagePath());
        item.setPetId(request.getPetId());
        return item;
    }
}
