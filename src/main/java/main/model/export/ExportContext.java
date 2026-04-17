package main.model.export;

import java.util.List;

public class ExportContext {

    private ExportStrategy strategy;

    public void setStrategy(ExportStrategy strategy) {
        this.strategy = strategy;
    }

    public String export(List<?> data) {
        return strategy.export(data);
    }
}