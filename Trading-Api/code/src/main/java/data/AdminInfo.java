package data;

import market.Admin;

public class AdminInfo {
    private int adminId;
    private String email;
    private String password;

    public AdminInfo(int adminId, String email, String password) {
        this.adminId = adminId;
        this.email = email;
        this.password = password;
    }

    public AdminInfo(Admin admin) {
        this.adminId = admin.getId();
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
