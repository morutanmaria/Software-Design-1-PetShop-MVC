package main.model.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class JsonExportStrategy implements ExportStrategy{
    public String export(List<?> data) {
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (Exception e) {
            return "Error generating JSON";
        }
    }
}