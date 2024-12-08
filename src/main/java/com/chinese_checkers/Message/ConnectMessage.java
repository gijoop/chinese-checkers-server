package com.chinese_checkers.Message;

import com.chinese_checkers.Player.CheckerColor;

public class ConnectMessage extends Message {
    private String name;
    private CheckerColor color;

    public ConnectMessage() {
        this.type = "connect";
    }

    public ConnectMessage(String name) {
        this.type = "connect";
        this.name = name;
        this.color = null;
    }

    public ConnectMessage(String name, CheckerColor color) {
        this.type = "connect";
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public CheckerColor getColor() {
        return color;
    }
    
}
