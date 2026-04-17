package main.model.event;

public class ResourceEvent {
    private String type;
    private String entity;
    private Object payload;

    public ResourceEvent(String type, String entity, Object payload) {
        this.type = type;
        this.entity = entity;
        this.payload = payload;
    }

    public String getType() { return type; }
    public String getEntity() { return entity; }
    public Object getPayload() { return payload; }
}