package com.chinese_checkers;

import java.net.Socket;
import java.io.PrintWriter;
import java.util.Scanner;


public class Client {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    

    public Client(String serverAddress) throws Exception {
        socket = new Socket(serverAddress, 58901);

        // print incoming messages
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        out.println("Hello everyone!");

        while (in.hasNextLine()) {
            System.out.println(in.nextLine());
        }

        socket.close();
    }
}
