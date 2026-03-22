package main.model.service;

import main.model.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item save(Item item);
    List<Item> findAll();
    Optional<Item> findById(Integer id);
    void deleteById(Integer id);
}
