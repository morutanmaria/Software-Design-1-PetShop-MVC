package main.controller;

import main.model.entity.Pet;
import main.model.entity.Role;
import main.model.entity.User;
import main.model.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedUser") != null;
    }

    private User getLoggedUser(HttpSession session) {
        return (User) session.getAttribute("loggedUser");
    }

    private boolean isAdmin(HttpSession session) {
        User user = getLoggedUser(session);
        return user != null && user.getRole() == Role.ADMIN;
    }

    private boolean isSeller(HttpSession session) {
        User user = getLoggedUser(session);
        return user != null && user.getRole() == Role.SELLER;
    }

    @GetMapping
    public String getAllPets(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model, HttpSession session) {

        List<Pet> pets = petService.searchAndSort(search, sortField, sortDir);
        if (pets == null) pets = new ArrayList<>();

        model.addAttribute("pets", pets);
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        model.addAttribute("search", search);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "pets";
    }

    @GetMapping("/add")
    public String addPetPage(HttpSession session) {
        if (!isLoggedIn(session) || !(isAdmin(session) || isSeller(session))) {
            return "redirect:/login";
        }
        return "pet-form";
    }

    @PostMapping("/save")
    public String savePet(@RequestParam String name,
                          @RequestParam String species,
                          @RequestParam String breed,
                          @RequestParam Integer age,
                          @RequestParam Double price,
                          HttpSession session) {

        if (!isLoggedIn(session) || !(isAdmin(session) || isSeller(session))) {
            return "redirect:/login";
        }

        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecies(species);
        pet.setBreed(breed);
        pet.setAge(age);
        pet.setPrice(BigDecimal.valueOf(price));
        pet.setAvailable(true);

        petService.savePet(pet);
        return "redirect:/pets";
    }

    @GetMapping("/edit/{id}")
    public String editPetPage(@PathVariable Integer id, Model model, HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login";
        }

        Optional<Pet> petOpt = petService.getPetById(id);
        if (petOpt.isEmpty()) {
            return "redirect:/pets";
        }

        model.addAttribute("pet", petOpt.get());
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

        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login";
        }

        Optional<Pet> petOpt = petService.getPetById(id);
        if (petOpt.isPresent()) {
            Pet pet = petOpt.get();
            pet.setName(name);
            pet.setSpecies(species);
            pet.setBreed(breed);
            pet.setAge(age);
            pet.setPrice(BigDecimal.valueOf(price));
            petService.updatePet(pet);
        }

        return "redirect:/pets";
    }

    @GetMapping("/delete/{id}")
    public String deletePet(@PathVariable Integer id, HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login";
        }
        petService.deletePetById(id);
        return "redirect:/pets";
    }
}