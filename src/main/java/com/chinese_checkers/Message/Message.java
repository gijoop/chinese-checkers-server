package com.chinese_checkers.Message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;


@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MoveMessage.class, name = "move"),
    @JsonSubTypes.Type(value = JoinMessage.class, name = "join"),
    @JsonSubTypes.Type(value = AcknowledgeMessage.class, name = "acknowledge")
    // Add more message types as needed
})
public abstract class Message {
    protected String type;

    public abstract String toString();

    public String getType() {
        return type;
    }

    public static Message fromJson(String json){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, Message.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
