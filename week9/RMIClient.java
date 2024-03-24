package tuan9;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    public static void main(String[] args) {
        try {
            // Tìm kiếm registry trên cổng 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 12345);

            // Lấy đối tượng calculator từ registry
            Calculator calculator = (Calculator) registry.lookup("CalculatorService");

            // Gọi phương thức từ xa
            int result = calculator.add(5, 10);
            System.out.println("Ket qua: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}