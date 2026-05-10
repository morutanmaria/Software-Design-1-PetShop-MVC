package main.controller;

import main.client.ItemClient;
import main.dto.ItemDTO;
import main.dto.LoggedUser;
import main.dto.PetDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    private LoggedUser getLoggedUser(HttpSession session) {
        return (LoggedUser) session.getAttribute("loggedUser");
    }

    private boolean isAdmin(HttpSession session) {
        LoggedUser u = getLoggedUser(session);
        return u != null && "ADMIN".equals(u.getRole());
    }

    private boolean isAdminOrSeller(HttpSession session) {
        LoggedUser u = getLoggedUser(session);
        return u != null && ("ADMIN".equals(u.getRole()) || "SELLER".equals(u.getRole()));
    }

    @GetMapping
    public String getAllItems(Model model) {
        List<ItemDTO> items = itemClient.getAllItems();
        model.addAttribute("items", items);
        return "items";
    }

    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        if (!isAdminOrSeller(session)) return "redirect:/login";
        List<PetDTO> pets = itemClient.getAvailablePets();
        model.addAttribute("pets", pets);
        model.addAttribute("item", new ItemDTO());
        return "add-item";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam String name,
                          @RequestParam String description,
                          @RequestParam double price,
                          @RequestParam String type,
                          @RequestParam(required = false) String imagePath,
                          @RequestParam Integer petId,
                          HttpSession session) {

        if (!isAdminOrSeller(session)) return "redirect:/login";
        itemClient.createItem(name, description, price, type, imagePath, petId);
        return "redirect:/items";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/items";
        ItemDTO item = itemClient.getItemById(id);
        if (item == null) return "redirect:/items";
        List<PetDTO> pets = itemClient.getAvailablePets();
        model.addAttribute("item", item);
        model.addAttribute("pets", pets);
        return "edit-item";
    }

    @PostMapping("/update")
    public String updateItem(@RequestParam Integer id,
                             @RequestParam String name,
                             @RequestParam String description,
                             @RequestParam double price,
                             @RequestParam String type,
                             @RequestParam(required = false) String imagePath,
                             @RequestParam Integer petId,
                             HttpSession session) {

        if (!isAdmin(session)) return "redirect:/items";
        itemClient.updateItem(id, name, description, price, type, imagePath, petId);
        return "redirect:/items";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/items";
        itemClient.deleteItem(id);
        return "redirect:/items";
    }
}
