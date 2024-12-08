package com.chinese_checkers.Message;

public class MoveMessage extends Message {
    private String from;
    private String to;

    public MoveMessage() {
        this.type = "move";
    }

    public MoveMessage(String from, String to) {
        this.type = "move";
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
