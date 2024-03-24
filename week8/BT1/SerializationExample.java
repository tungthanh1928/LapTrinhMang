package tuan8.BT1;
import tuan8.BT1.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SerializationExample {
    public static void main(String[] args) {
        Person person = new Person("John Doe", 30);
        
        try (FileOutputStream fileOut = new FileOutputStream("person.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            
            out.writeObject(person);
            System.out.println("Doi tuong da duoc tuan tu hoa vao file person.ser");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}