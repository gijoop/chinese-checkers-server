package com.chinese_checkers;

import java.net.Socket;
import java.io.PrintWriter;
import java.util.Scanner;

import com.chinese_checkers.Message.ConnectMessage;
import com.chinese_checkers.Message.MoveMessage;


public class Client_B {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private String serverAddress;
    
    public Client_B(String serverAddress) throws Exception {
        this.serverAddress = serverAddress;
    }

    public void connect() throws Exception {
        socket = new Socket(serverAddress, 58901);

        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        Thread.sleep(5000);
        
        out.println(new ConnectMessage("test_delay").toJson());
        System.out.println(in.nextLine());
        Thread.sleep(5000);

        socket.close();
    }

    public static void main(String[] args) throws Exception {
        try {
            Client_B client = new Client_B("localhost");
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
