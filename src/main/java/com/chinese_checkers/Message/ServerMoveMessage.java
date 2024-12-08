package com.chinese_checkers.Message;

public class ServerMoveMessage extends MoveMessage {
    private int playerID;

    public ServerMoveMessage() {
        this("", "", 0);
    }

    public ServerMoveMessage(String from, String to, int playerID) {
        super(from, to);
        this.type = "serverMove";
        this.playerID = playerID;
    }

    public ServerMoveMessage(MoveMessage moveMessage, int playerID) {
        super(moveMessage.getFrom(), moveMessage.getTo());
        this.type = "serverMove";
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String toString() {
        return "ServerMoveMessage: from: " + from + " to: " + to + " playerID: " + playerID;
    }
}
