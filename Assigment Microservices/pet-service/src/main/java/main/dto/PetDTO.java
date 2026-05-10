package main.dto;

import java.math.BigDecimal;

public class PetDTO {
    private Integer id;
    private String name;
    private String species;
    private String breed;
    private String gender;
    private Integer age;
    private BigDecimal price;
    private boolean available;
    private String imagePath;

    public PetDTO() {}

    public PetDTO(Integer id, String name, String species, String breed,
                  String gender, Integer age, BigDecimal price,
                  boolean available, String imagePath) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.age = age;
        this.price = price;
        this.available = available;
        this.imagePath = imagePath;
    }

    public static PetDTO from(main.model.entity.Pet pet) {
        return new PetDTO(
            pet.getId(), pet.getName(), pet.getSpecies(), pet.getBreed(),
            pet.getGender(), pet.getAge(), pet.getPrice(),
            pet.isAvailable(), pet.getImagePath()
        );
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
