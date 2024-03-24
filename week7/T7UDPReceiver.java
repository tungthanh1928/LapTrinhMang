package tuan7;
import java.net.*;
public class T7UDPReceiver {
    public static void main(String[] args) {
        try {
            //Tao doi tuong socket
            DatagramSocket socket=new DatagramSocket(12345);
            //tao 1 mang byte de luu du lieu nhan duoc
            byte[] nhan=new byte[1024];
            //tao packet nhan du lieu
            DatagramPacket packet=
                    new DatagramPacket(nhan, nhan.length);
            //nhan goi du lieu
            socket.receive(packet);
            //lay du lieu tu goi nhan
            String receiveData=
                    new String(packet.getData(),0,packet.getLength());
            System.out.println("Du lieu nhan duoc: "+receiveData);
            //dong ket noi
            socket.close();
        } catch (Exception e) {
        }
    }
}
