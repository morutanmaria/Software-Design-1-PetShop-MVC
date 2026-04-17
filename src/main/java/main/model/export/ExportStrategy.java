package main.model.export;

import java.util.List;
public interface ExportStrategy {
    String export(List<?> data);
}