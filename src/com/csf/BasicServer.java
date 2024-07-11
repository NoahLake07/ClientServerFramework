package com.csf;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * BasicServer class handles incoming client connections and facilitates message broadcasting.
 */
public class BasicServer {
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    /**
     * Default constructor for BasicServer.
     */
    public BasicServer() {}

    /**
     * Starts the server on the specified port.
     * @param port The port number to start the server on.
     */
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                new ClientHandler(serverSocket.accept(), this).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Broadcasts a message to all connected clients except the sender.
     * @param message The message to broadcast.
     * @param excludeClient The client to exclude from the broadcast.
     */
    public void broadcast(String message, ClientHandler excludeClient) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != excludeClient) {
                    client.send(message);
                }
            }
        }
    }

    /**
     * Adds a client to the list of connected clients.
     * @param clientHandler The client to add.
     */
    public void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    /**
     * Removes a client from the list of connected clients.
     * @param clientHandler The client to remove.
     */
    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}
