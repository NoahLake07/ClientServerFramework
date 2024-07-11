package com.csf;

import java.io.*;
import java.net.*;
import java.util.*;

public class BasicServer {
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public BasicServer() {
        // Default constructor
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            while (true) {
                new ClientHandler(serverSocket.accept(), this).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message, ClientHandler excludeClient) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != excludeClient) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    /*
    public static void main(String[] args) {
        // Start the server
        BasicServer server = new BasicServer();
        new Thread(() -> server.start(5000)).start(); // Run the server in a new thread
    }
    */
}
