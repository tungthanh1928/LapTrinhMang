package Models;

public class Session extends Thread{
    String hashKey;
    public boolean isRunning = true;
    public Session(String hashKey){
        this.hashKey = hashKey;
        System.out.println("[Session] " + hashKey + " actived !");
    }
    @Override
    public void run() {
        while(true){
            try {
                if(!isRunning){
                    AuthenciationServiceImp.clientSocket.writer.println(hashKey);
                    isRunning = false;
                    System.out.println("[Session] " + hashKey + " ended !");
                    break;
                }
                isRunning = false;
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }
    
}
