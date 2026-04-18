package main.model.export;

import java.util.List;
public interface ExportStrategy {
    byte[] export(Object data) throws Exception;;
}