package main.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class ExportController {

    private final RestTemplate restTemplate;
    private final String exportServiceUrl;

    public ExportController(RestTemplate restTemplate,
                            @Value("${export.service.url}") String exportServiceUrl) {
        this.restTemplate = restTemplate;
        this.exportServiceUrl = exportServiceUrl;
    }

    @GetMapping("/export/pets")
    public ResponseEntity<byte[]> exportPets(
            @RequestParam String format,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDir) {

        StringBuilder url = new StringBuilder(exportServiceUrl + "/export/pets?format=" + format);
        if (name != null)      url.append("&search=").append(name);
        if (sortField != null) url.append("&sortField=").append(sortField);
        if (sortDir != null)   url.append("&sortDir=").append(sortDir);

        ResponseEntity<byte[]> response = restTemplate.getForEntity(url.toString(), byte[].class);

        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getBody());
    }

    @GetMapping("/export/items")
    public ResponseEntity<byte[]> exportItems(@RequestParam String format) {
        String url = exportServiceUrl + "/export/items?format=" + format;
        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getBody());
    }
}
