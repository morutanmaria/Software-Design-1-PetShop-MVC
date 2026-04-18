package main.model.service;

import main.model.export.CsvExportStrategy;
import main.model.export.ExportContext;

import main.model.export.JsonExportStrategy;
import main.model.export.XmlExportStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportService {

    public byte[] export(List<?> data, String format) throws Exception {

        ExportContext context = new ExportContext();

        switch (format.toLowerCase()) {
            case "json":
                context.setStrategy(new JsonExportStrategy());
                break;
            case "csv":
                context.setStrategy(new CsvExportStrategy());
                break;
            case "xml":
                context.setStrategy(new XmlExportStrategy());
                break;
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }

        return context.export(data);
    }
}