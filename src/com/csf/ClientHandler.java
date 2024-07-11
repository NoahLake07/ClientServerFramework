package com.csf;

import java.io.*;
import java.net.*;

/**
 * ClientHandler class handles communication with a single client. A BasicServer will create a new ClientHandler for each client that connects.
 */
public class ClientHandler extends Thread {
    private Socket socket;
    private BasicServer server;
    private PrintWriter out;

    /**
     * Constructor for ClientHandler.
     * @param socket The socket connected to the client.
     * @param server The server that accepted the connection.
     */
    public ClientHandler(Socket socket, BasicServer server) {
        this.socket = socket;
        this.server = server;
        server.addClient(this);
    }

    /**
     * Method to handle received messages. Can be overridden by subclasses.
     * @param message The message received from the client.
     */
    public void receivedMessage(String message) {}

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String message;
            while ((message = in.readLine()) != null) {
                receivedMessage(message);
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

    /**
     * Sends a message to the client.
     * @param message The message to send.
     */
    public void send(String message) {
        out.println(message);
    }
}
