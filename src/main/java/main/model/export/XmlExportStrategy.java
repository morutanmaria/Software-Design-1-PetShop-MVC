package main.model.export;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import main.model.entity.Item;
import main.model.entity.Pet;
import org.springframework.stereotype.Component;

import java.util.List;
@Component("xml")
public class XmlExportStrategy implements ExportStrategy{

    @Override
    public byte[] export(Object data) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsBytes(data);
    }
}