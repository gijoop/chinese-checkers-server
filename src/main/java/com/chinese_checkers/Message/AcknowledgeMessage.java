package com.chinese_checkers.Message;

public class AcknowledgeMessage extends Message {
    private String info;

    public AcknowledgeMessage() {
        this.type = "acknowledge";
    }

    public AcknowledgeMessage(String info) {
        this.type = "acknowledge";
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
