package main.model.export;

import main.model.entity.Item;
import main.model.entity.Pet;

import java.util.List;

public class XmlExportStrategy implements ExportStrategy{

    @Override
    public String export(List<?> data) {

        if (data.isEmpty()) return "<empty/>";

        Object first = data.get(0);
        StringBuilder sb = new StringBuilder();

        if (first instanceof Pet) {
            sb.append("<pets>");

            for (Object obj : data) {
                Pet p = (Pet) obj;
                sb.append("<pet>").append("<id>").append(p.getId()).append("</id>").append("<name>").append(p.getName()).append("</name>").append("</pet>");
            }

            sb.append("</pets>");

        }
        else if (first instanceof Item) {
            sb.append("<items>");

            for (Object obj : data) {
                Item i = (Item) obj;
                sb.append("<item>").append("<id>").append(i.getId()).append("</id>").append("<name>").append(i.getName()).append("</name>").append("</item>");
            }

            sb.append("</items>");
        }

        return sb.toString();
    }
}