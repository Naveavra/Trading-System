package database.dtos;
import database.HibernateUtil;
import domain.user.Member;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.messageRelated.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Entity
@Table(name = "users")
public class MemberDto {

    @Id
    int id;
    String email;
    String birthday;
    String password;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    protected List<NotificationDto> notifications;


    public MemberDto() {

    }
    public MemberDto(int id, String email, String password, String birthday){
        this.id = id;
        this.email = email;
        this.birthday = birthday;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<NotificationDto> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for(Notification n : notifications)
            notificationDtos.add(new NotificationDto(this, n.getNotification().toString(), n.getOpcode().toString()));
        this.notifications = notificationDtos;
    }
}
