import java.net.*;
import java.io.*;

public class run {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        try {
            // 1. Tạo đối tượng InetAddress
            InetAddress address = InetAddress.getByName("java.io");

            // 2. Lấy tên và địa chỉ IP máy chủ
            System.out.println("Host Name: " + address.getHostName());
            System.out.println("IP Address: " + address.getHostAddress());

            // 3. Kiểm tra IP4 hay IP6
            if (address instanceof Inet4Address) {
                System.out.println("IPv4 Address");
            } else if (address instanceof Inet6Address) {
                System.out.println("IPv6 Address");
            }

            // 4. Phân tích URL
            URL url = new URL("http://java.io/");
            System.out.println("Protocol: " + url.getProtocol());
            System.out.println("Host: " + url.getHost());
            System.out.println("Path: " + url.getPath());

            // 5. Đọc dữ liệu từ 1 trang web
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

            // 6. Đọc dữ liệu từ 1 trang web và ghi ra file
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.html"));
            while ((line = reader2.readLine()) != null) {
                writer.write(line);
            }
            reader2.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}