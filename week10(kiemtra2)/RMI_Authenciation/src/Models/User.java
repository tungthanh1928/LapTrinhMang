package Models;

public class User {
    public String username;
    public String password;
    public String fullName;
    private String hashKey;
    public long lastTimeActived = 0;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.hashKey = Util.encodeToMD5(username + "." + password);
    }
    
    protected void setHashKey(String key){
        this.hashKey = key;
    }
    public String getHashKey(){
        return hashKey;
    }
}
