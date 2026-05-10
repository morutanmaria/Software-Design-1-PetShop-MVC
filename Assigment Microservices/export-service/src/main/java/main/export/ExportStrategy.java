package main.export;

public interface ExportStrategy {
    byte[] export(Object data) throws Exception;
}
