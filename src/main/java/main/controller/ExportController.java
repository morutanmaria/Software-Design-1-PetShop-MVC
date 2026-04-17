package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import main.model.dto.ItemDTO;
import main.model.dto.PetDTO;
import main.model.entity.Item;
import main.model.entity.Pet;
import main.model.service.ExportService;
import main.model.service.ItemService;
import main.model.service.PetService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ExportController {

    private final PetService petService;
    private final ItemService itemService;
    private final ExportService exportService;

    public ExportController(PetService petService, ItemService itemService, ExportService exportService) {
        this.petService = petService;
        this.itemService = itemService;
        this.exportService = exportService;
    }

    @GetMapping("/export/pets")
    public ResponseEntity<byte[]> exportPets(
            @RequestParam String format,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDir
    ) throws Exception {

        List<PetDTO> pets = petService.searchAndSort(name, sortField, sortDir)
                .stream()
                .map(this::toDTO)
                .toList();

        return buildFileResponse(pets, format, "pets");
    }

    @GetMapping("/export/items")
    public ResponseEntity<byte[]> exportItems(
            @RequestParam String format
    ) throws Exception {

        List<ItemDTO> items = itemService.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        return buildFileResponse(items, format, "items");
    }

    private ResponseEntity<byte[]> buildFileResponse(Object data, String format, String baseName) throws Exception {
        byte[] fileData;
        String filename;
        String contentType;

        switch (format.toLowerCase()) {

            case "json" -> {
                ObjectMapper mapper = new ObjectMapper();
                fileData = mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsBytes(data);
                filename = baseName + ".json";
                contentType = "application/json";
            }

            case "csv" -> {
                fileData = toCsv(data).getBytes();
                filename = baseName + ".csv";
                contentType = "text/csv";
            }

            case "xml" -> {
                XmlMapper xmlMapper = new XmlMapper();
                fileData = xmlMapper.writeValueAsBytes(data);
                filename = baseName + ".xml";
                contentType = "application/xml";
            }
            default -> throw new IllegalArgumentException("Invalid format: " + format);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType))
                .body(fileData);
    }

    private String toCsv(Object data) {

        StringBuilder sb = new StringBuilder();

        if (data instanceof List<?> list && !list.isEmpty()) {
            Object first = list.get(0);
            if (first instanceof PetDTO) {
                sb.append("id,name,species,breed,gender,age,price,available\n");

                for (PetDTO p : (List<PetDTO>) list) {
                    sb.append(p.getId()).append(",")
                            .append(p.getName()).append(",")
                            .append(p.getSpecies()).append(",")
                            .append(p.getBreed()).append(",")
                            .append(p.getGender()).append(",")
                            .append(p.getAge()).append(",")
                            .append(p.getPrice()).append(",")
                            .append(p.isAvailable())
                            .append("\n");
                }
            }

            else if (first instanceof ItemDTO) {
                sb.append("id,name,description,price,type,petName\n");

                for (ItemDTO i : (List<ItemDTO>) list) {
                    sb.append(i.getId()).append(",")
                            .append(i.getName()).append(",")
                            .append(i.getDescription()).append(",")
                            .append(i.getPrice()).append(",")
                            .append(i.getType()).append(",")
                            .append(i.getPetName())
                            .append("\n");
                }
            }
        }

        return sb.toString();
    }

    private PetDTO toDTO(Pet pet) {
        return new PetDTO(
                pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getGender(),
                pet.getAge(),
                pet.getPrice(),
                pet.isAvailable()
        );
    }
    private ItemDTO toDTO(Item i) {
        return new ItemDTO(
        i.getId(),
        i.getName(),
        i.getDescription(),
        i.getPrice(),
        String.valueOf(i.getType()),
        i.getPet() != null ? i.getPet().getName() : null
        );
    }
}