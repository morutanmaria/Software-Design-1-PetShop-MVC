package main.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("xml")
public class XmlExportStrategy implements ExportStrategy {

    @Override
    public byte[] export(Object data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<list>\n");

        if (data instanceof List<?> list) {
            for (Object item : list) {
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item);
                xml.append("  <item>\n");
                mapper.readTree(json).fields().forEachRemaining(entry ->
                        xml.append("    <").append(entry.getKey()).append(">")
                                .append(entry.getValue().asText())
                                .append("</").append(entry.getKey()).append(">\n")
                );
                xml.append("  </item>\n");
            }
        }

        xml.append("</list>");
        return xml.toString().getBytes();
    }
}