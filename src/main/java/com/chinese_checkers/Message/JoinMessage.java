package com.chinese_checkers.Message;

public class JoinMessage extends Message {
    private String name;

    public JoinMessage() {
        this("defaultName"); // Default values can be adjusted as needed
    }

    public JoinMessage(String name) {
        this.type = "join";
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public String toString() {
        return "JoinMessage: name: " + name;
    }
    
}
