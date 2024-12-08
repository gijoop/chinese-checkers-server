package com.chinese_checkers.Message;

public class MoveMessage extends Message {
    protected String from;
    protected String to;

    public MoveMessage() {
        this("", "");
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

    public String toString() {
        return "MoveMessage: from: " + from + " to: " + to;
    }
}
