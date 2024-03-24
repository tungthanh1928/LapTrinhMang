package tuan8.BT1;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class DeserializationExample {
    public static void main(String[] args) {
        try (FileInputStream fileIn = new FileInputStream("person.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            
            Person person = (Person) in.readObject();
            System.out.println("Doi tuong da duoc doc tu file person.ser: " + person);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}