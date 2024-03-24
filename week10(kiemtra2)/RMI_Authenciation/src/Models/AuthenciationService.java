package Models;
import java.rmi.*;

public interface AuthenciationService extends Remote{
    public String login(String username, String password) throws RemoteException;
    public String getFullname(String hashKey) throws RemoteException;
    public boolean register(String username, String password, String fullName) throws RemoteException;
}
