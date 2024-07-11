package com.csf;

import java.io.*;
import java.net.*;

/**
 * BasicClient class handles connecting to a server and receiving messages.
 */
public class BasicClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String serverAddress;
    private int serverPort;

    /**
     * Constructor for BasicClient.
     * @param serverAddress The address of the server to connect to.
     * @param serverPort The port of the server to connect to.
     */
    public BasicClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    /**
     * Starts the connection to the server.
     */
    public void startConnection() {
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new ReadThread(in).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the server.
     * @param message The message to send.
     */
    protected void send(String message) {
        out.println(message);
    }

    /**
     * Method to handle received messages. Can be overridden by subclasses.
     * @param message The message received from the server.
     */
    public void receivedMessage(String message) {}

    /**
     * ReadThread class reads messages from the server.
     */
    class ReadThread extends Thread {
        private BufferedReader reader;

        /**
         * Constructor for ReadThread.
         * @param reader The BufferedReader to read messages from.
         */
        public ReadThread(BufferedReader reader) {
            this.reader = reader;
        }

        public void run() {
            String response;
            try {
                while ((response = reader.readLine()) != null) {
                    receivedMessage(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
