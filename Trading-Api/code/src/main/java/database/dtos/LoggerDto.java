package database.dtos;


import jakarta.persistence.*;

@Entity
@Table(name = "logger")
public class LoggerDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String status;
    private String content;
    private String time;
    private String userName;

    public LoggerDto() {
    }

    public LoggerDto(String status, String content, String time, String userName) {
        this.status = status;
        this.content = content;
        this.time = time;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
