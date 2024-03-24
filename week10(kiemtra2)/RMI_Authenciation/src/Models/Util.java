package Models;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static String encodeToMD5(String input) {
        try {
            // Tạo một đối tượng MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Cập nhật bộ mã băm với dữ liệu đầu vào
            md.update(input.getBytes());
            // Mã hóa dữ liệu và chuyển đổi kết quả thành mảng byte
            byte[] byteData = md.digest();
            
            // Chuyển đổi mảng byte thành chuỗi hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : byteData) {
                // Đổi từ byte dạng số âm thành dạng không âm (0-255)
                String hex = Integer.toHexString(0xFF & b);
                // Thêm ký tự 0 vào trước nếu độ dài của chuỗi hex nhỏ hơn 2
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
