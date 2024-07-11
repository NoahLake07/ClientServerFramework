package com.csf;

import java.io.*;
import java.net.*;

public class BasicClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String serverAddress;
    private int serverPort;

    public BasicClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void startConnection() {
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new ReadThread(in).start();
            sendMessage("Hello Server!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendMessage(String message) {
        out.println(message);
    }

    class ReadThread extends Thread {
        private BufferedReader reader;

        public ReadThread(BufferedReader reader) {
            this.reader = reader;
        }

        public void run() {
            String response;
            try {
                while ((response = reader.readLine()) != null) {
                    System.out.println("Server: " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    public static void main(String[] args) {
        BasicClient client = new BasicClient("0.0.0.0", 5000); // Replace with your Cloudflare subdomain
        client.startConnection();
    }
    */
}
