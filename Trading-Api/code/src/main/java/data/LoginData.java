package data;

import utils.infoRelated.LoginInformation;

import java.util.List;
import java.util.stream.Collectors;

public class LoginData {
    private int userId;
    private List<String> notifications;
    public LoginData(LoginInformation loginInformation) {
        this.userId = loginInformation.getUserId();
        this.notifications = loginInformation.getNotifications()
                .stream()
                .map(notification -> notification.toString())
                .collect(Collectors.toList());
    }

    public int getUserId() {
        return userId;
    }

    public List<String> getNotifications() {
        return notifications;
    }
}
