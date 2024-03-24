package Client;

import Models.Session;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

//1571020223 Chu Hoàng Sơn CNTT15-04
public class RunClient {

    //1571020223 Chu Hoàng Sơn CNTT15-04
    public static void main(String[] args) {
        //1571020223 Chu Hoàng Sơn CNTT15-04
        //1571020223 Chu Hoàng Sơn CNTT15-04
        UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
        try {
            UIManager.setLookAndFeel(lafInfo[3].getClassName());
        } catch (Exception e) {
        }
        
//        new FrmDashboard(name).setVisible(true);
        new FrmLogin().setVisible(true);
    }
    
}
