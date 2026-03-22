package main.controller;

import jakarta.servlet.http.HttpSession;
import main.model.entity.*;
import main.model.service.ItemService;
import main.model.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final PetService petService;

    public ItemController(ItemService itemService, PetService petService) {
        this.itemService = itemService;
        this.petService = petService;
    }

    @GetMapping
    public String getAllItems(Model model) {
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        return "items";
    }

    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || (user.getRole() != Role.ADMIN && user.getRole() != Role.SELLER)) {
            return "redirect:/login";
        }

        model.addAttribute("item", new Item());
        model.addAttribute("pets", petService.getAllPets());
        return "add-item";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item,
                          @RequestParam Integer petId,
                          HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");

        if (user == null || (user.getRole() != Role.ADMIN && user.getRole() != Role.SELLER)) {
            return "redirect:/login";
        }

        Pet pet = petService.getPetById(petId).orElseThrow();
        item.setPet(pet);
        itemService.save(item);
        return "redirect:/items";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ADMIN) {
            return "redirect:/items";
        }

        Item item = itemService.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        model.addAttribute("item", item);

        model.addAttribute("pets", petService.getAllPets());

        List<String> breeds = petService.getAllPets().stream()
                .map(Pet::getBreed)
                .distinct()
                .toList();
        model.addAttribute("breeds", breeds);

        return "edit-item";
    }

    @PostMapping("/update")
    public String updateItem(@RequestParam Integer id,
                             @RequestParam String name,
                             @RequestParam String description,
                             @RequestParam double price,
                             @RequestParam ItemType type,
                             @RequestParam(required = false) String imagePath,
                             @RequestParam Integer petId,
                             HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ADMIN) {
            return "redirect:/items";
        }

        Item item = itemService.findById(id).orElseThrow();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setType(type);
        item.setImagePath(imagePath);

        Pet pet = petService.getPetById(petId).orElseThrow();
        item.setPet(pet);

        itemService.save(item);
        return "redirect:/items";
    }
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ADMIN) {
            return "redirect:/items";
        }
        itemService.deleteById(id);
        return "redirect:/items";
    }
}