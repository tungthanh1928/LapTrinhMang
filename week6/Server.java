package week6;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.*;

public class Server {
    private static final int PORT = 44444;
    // 1571020177
    private static List<ClientHandler> clients = new ArrayList<>();
    // 1571020177
    private static final int MAX_CONNECTIONS = 10; // Số kết nối tối đa cho phép
    // 1571020177

    public static void main(String[] args) {
        // 1571020177
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                if (clients.size() < MAX_CONNECTIONS) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket);

                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);

                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();
                } else {
                    System.out.println("Max connections reached. Cannot accept new clients.");
                    // Có thể thực hiện các hành động khác tại đây
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        // 1571020177
        private Socket Msv1571020177;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            // 1571020177
            this.Msv1571020177 = socket;
            try {
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            // 1571020177
            try {
                String name = reader.readLine(); // Đọc tên từ client
                System.out.println(name + " has joined the chat.");
                broadcastMessage(name + " has joined the chat.");

                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received: " + message);
                    if (message.startsWith("[FILE]")) {
                        String fileName = message.substring(6);
                        receiveAndBroadcastFile(fileName);
                    } else {
                        broadcastMessage(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    Msv1571020177.close();
                    clients.remove(this); // Loại bỏ client đã đóng kết nối khỏi danh sách
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcastMessage(String message) {
            // 1571020177
            if (message.startsWith("[FILEDATA]")) {
                String fileName = message.substring(10);
                try {
                    OutputStream outputStream = Msv1571020177.getOutputStream();
                    File file = new File(fileName);
                    byte[] fileData = Files.readAllBytes(file.toPath());
                    outputStream.write(fileData);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                for (ClientHandler client : clients) {
                    client.writer.println(message);
                }
            }
        }

        private void receiveAndBroadcastFile(String fileName) throws IOException {
            // 1571020177
            // Broadcast file name to all clients
            for (ClientHandler client : clients) {
                if (client != this) {
                    client.writer.println("[FILE]" + fileName);
                }
            }

            // Receive file data from client
            InputStream fileInputStream = Msv1571020177.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.close();

            // Broadcast file data to all clients
            File file = new File(fileName);
            byte[] fileData = Files.readAllBytes(file.toPath());
            for (ClientHandler client : clients) {
                if (client != this) {
                    client.writer.println("[FILEDATA]" + fileName);
                    client.Msv1571020177.getOutputStream().write(fileData);
                    client.writer.println("[ENDFILEDATA]");
                }
            }
        }
    }
}