package main.dto;

import main.model.entity.Item;

public class ItemDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String type;
    private Integer petId;
    private String petName;

    public ItemDTO() {}

    public ItemDTO(Integer id, String name, String description,
                   Double price, String type, Integer petId, String petName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.petId = petId;
        this.petName = petName;
    }

    public static ItemDTO from(Item item, String petName) {
        return new ItemDTO(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.getPrice(),
            item.getType() != null ? item.getType().name() : null,
            item.getPetId(),
            petName
        );
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getPetId() { return petId; }
    public void setPetId(Integer petId) { this.petId = petId; }
    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }
}
