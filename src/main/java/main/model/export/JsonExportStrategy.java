package main.model.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Component("json")
public class JsonExportStrategy implements ExportStrategy{
    @Override
    public byte[] export(Object data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsBytes(data);
    }
}