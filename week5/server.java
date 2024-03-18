package week5;

import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) {
        // Đặt cổng cho server
        int port = 44444;

        try {
            // Tạo server socket
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server hoat dong tren cong: " + port);

            while (true) {
                // Chấp nhận kết nối từ client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Da ket noi voi " + clientSocket.getInetAddress());

                // Tạo đối tượng đọc dữ liệu từ client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Tạo đối tượng ghi dữ liệu đến client
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Nhận dữ liệu từ client
                String dataFromClient = in.readLine();
                System.out.println("Du lieu tu Client: " + dataFromClient);

                // Gửi lại dữ liệu cho client
                out.println("Da ket noi server thanh cong");

                // Đóng kết nối với client
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
