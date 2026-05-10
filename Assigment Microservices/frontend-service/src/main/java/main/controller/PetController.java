package main.controller;

import main.client.PetClient;
import main.dto.LoggedUser;
import main.dto.PetDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pets")
public class PetController {

    private final PetClient petClient;

    public PetController(PetClient petClient) {
        this.petClient = petClient;
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
    public String getAllPets(@RequestParam(required = false) String search,
                             @RequestParam(defaultValue = "name") String sortField,
                             @RequestParam(defaultValue = "asc") String sortDir,
                             Model model, HttpSession session) {

        List<PetDTO> pets = petClient.getAllPets(search, sortField, sortDir);
        model.addAttribute("pets", pets);
        model.addAttribute("search", search);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "pets";
    }

    @GetMapping("/add")
    public String addPetPage(HttpSession session) {
        if (!isAdminOrSeller(session)) return "redirect:/login";
        return "pet-form";
    }

    @PostMapping("/save")
    public String savePet(@RequestParam String name,
                          @RequestParam String species,
                          @RequestParam String breed,
                          @RequestParam Integer age,
                          @RequestParam Double price,
                          HttpSession session) {

        if (!isAdminOrSeller(session)) return "redirect:/login";
        petClient.createPet(name, species, breed, age, price);
        return "redirect:/pets";
    }

    @GetMapping("/edit/{id}")
    public String editPetPage(@PathVariable Integer id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        PetDTO pet = petClient.getPetById(id);
        if (pet == null) return "redirect:/pets";
        model.addAttribute("pet", pet);
        return "pet-edit";
    }

    @PostMapping("/update")
    public String updatePet(@RequestParam Integer id,
                            @RequestParam String name,
                            @RequestParam String species,
                            @RequestParam String breed,
                            @RequestParam Integer age,
                            @RequestParam Double price,
                            HttpSession session) {

        if (!isAdmin(session)) return "redirect:/login";
        petClient.updatePet(id, name, species, breed, age, price);
        return "redirect:/pets";
    }

    @GetMapping("/delete/{id}")
    public String deletePet(@PathVariable Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        petClient.deletePet(id);
        return "redirect:/pets";
    }
}
