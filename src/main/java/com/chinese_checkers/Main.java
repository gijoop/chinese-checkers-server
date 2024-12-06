package com.chinese_checkers;

/**
 * A server for a multi-player tic tac toe game. Loosely based on an example in
 * Deitel and Deitel’s “Java How to Program” book. For this project I created a
 * new application-level protocol called TTTP (for Tic Tac Toe Protocol), which
 * is entirely plain text. The messages of TTTP are:
 *
 * Client -> Server MOVE <n> QUIT
 *
 * Server -> Client WELCOME <char> VALID_MOVE OTHER_PLAYER_MOVED <n>
 * OTHER_PLAYER_LEFT VICTORY DEFEAT TIE MESSAGE <text>
 */
public class Main {
    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("Specify client or server");
            return;
        }
        if(args[0].equals("client")) {
            try {
                Client client = new Client("localhost");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(args[0].equals("server")) {
            Server gameServer = new Server(3);
        }
    }
}