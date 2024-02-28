package week5;

import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) {
        // Địa chỉ IP và cổng của server
        String serverAddress = "127.0.0.1";
        int serverPort = 44444;

        try {
            // Tạo socket để kết nối đến server
            Socket socket = new Socket(serverAddress, serverPort);

            // Tạo đối tượng đọc dữ liệu từ server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Tạo đối tượng ghi dữ liệu đến server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Gửi dữ liệu cho server
            out.println("Hello, server!");

            // Nhận dữ liệu từ server
            String dataFromServer = in.readLine();
            System.out.println("Du lieu tu Server: " + dataFromServer);

            // Đóng kết nối
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
