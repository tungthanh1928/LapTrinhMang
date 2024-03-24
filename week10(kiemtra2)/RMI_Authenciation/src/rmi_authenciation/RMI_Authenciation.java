package rmi_authenciation;

import Models.AuthenciationServiceImp;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author cr4zyb0t
 */
public class RMI_Authenciation {

    public static void main(String[] args) {
        try {
            int port = 12222;
            LocateRegistry.createRegistry(port);
            AuthenciationServiceImp obj = new AuthenciationServiceImp();
            obj.register("admin", "1", "AA");
            try {
                AuthenciationServiceImp.clientSocket.socket = new Socket("127.0.0.1",11111);
                AuthenciationServiceImp.clientSocket.writer = new PrintWriter(AuthenciationServiceImp.clientSocket.socket.getOutputStream(), true);
            } catch (Exception e) {
            }
            Naming.rebind("//localhost:"+ port +"/AuthenciationService", obj);
            System.out.println("RMI Service  is running on port " + port + " ...");
        } catch (MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
    
}
