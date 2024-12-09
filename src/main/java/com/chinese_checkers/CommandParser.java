package com.chinese_checkers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import com.chinese_checkers.comms.Message.Message;

public class CommandParser {

    private final ConcurrentHashMap<String, Consumer<Message>> commands = new ConcurrentHashMap<>();

    public CommandParser() {}

    public void parseCommand(Message msg) {
        if (commands.containsKey(msg.getType())) {
            commands.get(msg.getType()).accept(msg);
        } else {
            System.out.println("Command not found for type: " + msg.getType());
        }
    }

    public void addCommand(String msgType, Consumer<Message> action) {
        if (commands.containsKey(msgType)) {
            commands.remove(msgType);
        }
        commands.put(msgType, action);
    }

    public void clearCommands() {
        commands.clear();
    }
}

