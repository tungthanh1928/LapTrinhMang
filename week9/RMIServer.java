package tuan9;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            Calculator calculator = new CalculatorImpl();

            // Tạo registry trên cổng 1099
            Registry registry = LocateRegistry.createRegistry(12345);

            // Đăng ký đối tượng calculator với tên "CalculatorService"
            registry.rebind("CalculatorService", calculator);

            System.out.println("Server da san sang");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}