package database.dtos;
import database.HibernateUtil;
import domain.user.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Entity
@Table(name = "users")
public class MemberDto {

    @Id
    int id;
    String email;
    String birthday;
    String password;


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
}
