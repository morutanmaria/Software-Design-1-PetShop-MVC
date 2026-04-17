package main.model.export;


import main.model.entity.Item;
import main.model.entity.Pet;

import java.util.List;

public class CsvExportStrategy implements ExportStrategy {
    @Override
    public String export(List<?> data) {
        StringBuilder sb = new StringBuilder();
        if (data.isEmpty()) return "";

        Object first = data.get(0);

        if (first instanceof main.model.entity.Pet) {
            sb.append("id,name,species\n");

            for (Object obj : data) {
                Pet p = (Pet) obj;
                sb.append(p.getId()).append(",").append(p.getName()).append(",").append(p.getSpecies()).append("\n");
            }

        }
        else if (first instanceof main.model.entity.Item) {
            sb.append("id,name,price\n");

            for (Object obj : data) {
                Item i = (Item) obj;
                sb.append(i.getId()).append(",").append(i.getName()).append(",").append(i.getPrice()).append("\n");
            }
        }

        return sb.toString();
    }
}