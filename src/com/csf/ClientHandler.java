package com.csf;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private BasicServer server;
    private PrintWriter out;

    public ClientHandler(Socket socket, BasicServer server) {
        this.socket = socket;
        this.server = server;
        server.addClient(this);
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                server.broadcast(message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
