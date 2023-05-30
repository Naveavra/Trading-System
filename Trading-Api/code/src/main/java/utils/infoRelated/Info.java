package utils.infoRelated;

import domain.user.history.PurchaseHistory;
import org.json.JSONObject;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.LinkedList;
import java.util.List;

public class Info extends Information{

    private int id;
    private String email;
    private String birthday;
    private int age;
    private Role role;
    private List<Action> managerActions;
    private PurchaseHistory purchaseHistory;

    public Info(int id, String email, String birthday, int age){
        this.id = id;
        this.email = email;
        this.birthday = birthday;
        this.age = age;
        managerActions = new LinkedList<>();
        purchaseHistory = new PurchaseHistory(id);
    }

    public void addRole(Role role){this.role = role;}
    public void addManagerActions(List<Action> actions){
        managerActions.addAll(actions);
    }

    public List<Action> getManagerActions(){
        return managerActions;
    }



    public int getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }
    public int getAge(){
        return age;
    }
    @Override
    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.put("userId", getId());
        json.put("email", getEmail());
        json.put("birthday", getBirthday());
        json.put("age", getAge());
        json.put("purchaseHistory", purchaseHistory.toJson());
        json.put("role", role);
        json.put("managerPermissions", fromActionToString(getManagerActions()));
        return json;
    }
}
