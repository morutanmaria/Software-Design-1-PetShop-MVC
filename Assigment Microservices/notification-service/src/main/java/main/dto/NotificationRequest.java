package main.dto;

public class NotificationRequest {
    private String entity;
    private String name;
    private String type;

    public NotificationRequest() {}

    public String getEntity() { return entity; }
    public void setEntity(String entity) { this.entity = entity; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
