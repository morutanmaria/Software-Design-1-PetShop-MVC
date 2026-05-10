package main.controller;

import main.client.ItemClient;
import main.client.PetClient;
import main.dto.ItemDTO;
import main.dto.PetDTO;
import main.export.CsvExportStrategy;
import main.export.ExportStrategy;
import main.export.JsonExportStrategy;
import main.export.XmlExportStrategy;
import main.model.service.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ExportController {

    private final PetClient petClient;
    private final ItemClient itemClient;
    private final ExportService exportService;
    private final Map<String, ExportStrategy> strategies;

    public ExportController(PetClient petClient,
                            ItemClient itemClient,
                            ExportService exportService,
                            List<ExportStrategy> strategyList) {
        this.petClient = petClient;
        this.itemClient = itemClient;
        this.exportService = exportService;
        this.strategies = Map.of(
                "json", strategyList.stream().filter(s -> s instanceof JsonExportStrategy).findFirst().orElseThrow(),
                "csv",  strategyList.stream().filter(s -> s instanceof CsvExportStrategy).findFirst().orElseThrow(),
                "xml",  strategyList.stream().filter(s -> s instanceof XmlExportStrategy).findFirst().orElseThrow()
        );
    }

    @GetMapping("/export/pets")
    public ResponseEntity<byte[]> exportPets(
            @RequestParam String format,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDir) throws Exception {

        List<PetDTO> pets = petClient.searchAndSort(search, sortField, sortDir);
        return buildFileResponse(pets, format, "pets");
    }

    @GetMapping("/export/items")
    public ResponseEntity<byte[]> exportItems(
            @RequestParam String format) throws Exception {

        List<ItemDTO> items = itemClient.getAllItems();
        return buildFileResponse(items, format, "items");
    }

    private ResponseEntity<byte[]> buildFileResponse(Object data, String format, String baseName) throws Exception {
        ExportStrategy strategy = strategies.get(format.toLowerCase());

        if (strategy == null) {
            throw new IllegalArgumentException("Invalid format: " + format);
        }

        byte[] fileData = strategy.export(data);

        String contentType;
        String filename;

        switch (format.toLowerCase()) {
            case "json" -> { contentType = "application/json"; filename = baseName + ".json"; }
            case "csv"  -> { contentType = "text/csv";         filename = baseName + ".csv";  }
            case "xml"  -> { contentType = "application/xml";  filename = baseName + ".xml";  }
            default     -> throw new IllegalArgumentException("Invalid format: " + format);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType))
                .body(fileData);
    }
}
