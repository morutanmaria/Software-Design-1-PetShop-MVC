package main.model.service;

import main.dto.ItemDTO;
import main.dto.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    ItemDTO save(ItemRequest request);
    ItemDTO update(Integer id, ItemRequest request);
    void deleteById(Integer id);

    List<ItemDTO> findAll();
    Optional<ItemDTO> findById(Integer id);
    List<ItemDTO> findByPetId(Integer petId);
}
