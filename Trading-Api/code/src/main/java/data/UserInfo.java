package data;

public class UserInfo {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String birthday;

    public UserInfo(String email, String username, String password, String birthday) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.userId = 0;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
