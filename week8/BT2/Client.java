package tuan8.BT2;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);

            // Create object to send
            Person person = new Person("John Doe", 30);
            System.out.println("Object to send: " + person);

            // Send object to server
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(person);
            System.out.println("Sent object to server");

            // Receive response from server
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String response = (String) in.readObject();
            System.out.println("Received response from server: " + response);

            // Close connections
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}