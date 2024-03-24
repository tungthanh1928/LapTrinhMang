package tuan8.BT2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);

            System.out.println("Server is listening on port 12345...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket);

            // Receive object from client
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Person person = (Person) in.readObject();
            System.out.println("Received object from client: " + person);

            // Send response to client
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            String response = "Object received successfully";
            out.writeObject(response);
            System.out.println("Sent response to client: " + response);

            // Close connections
            in.close();
            out.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}