package utils;

public class Token {
    private String token;
    private int userId;
    private String userName;

    public Token(String t,int ui,String un){
        token=t;
        userId = ui;
        userName=un;
    }
    public int getUserId(){
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }
}
