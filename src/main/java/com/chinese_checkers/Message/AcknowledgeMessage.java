package com.chinese_checkers.Message;

public class AcknowledgeMessage extends Message {
    private String info;

    public AcknowledgeMessage() {
        this("");
    }

    public AcknowledgeMessage(String info) {
        this.type = "acknowledge";
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public String toString() {
        return "AcknowledgeMessage: " + info;
    }
}
