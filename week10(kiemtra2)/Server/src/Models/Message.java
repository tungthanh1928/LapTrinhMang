package Models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
//1571020223 Chu Hoàng Sơn CNTT15-04
public class Message {
    public int idSend;
    public String nameSend;
    public String message;
    public String time;
    //1571020223 Chu Hoàng Sơn CNTT15-04
    public Message(int idSend, String nameSend, String message) {
        this.idSend = idSend;
        this.nameSend = nameSend;
        this.message = message;
        this.time = new SimpleDateFormat("[HH:mm:ss - dd/MM/yyyy] ").format(Calendar.getInstance().getTime());
    }
}
