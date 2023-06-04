package utils.infoRelated;

import domain.user.PurchaseHistory;
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
    private List<Action> actions;
    private PurchaseHistory purchaseHistory;

    public Info(int id, String email, String birthday, int age){
        this.id = id;
        this.email = email;
        this.birthday = birthday;
        this.age = age;
        actions = new LinkedList<>();
        purchaseHistory = new PurchaseHistory(id);
    }

    public void addRole(Role role){this.role = role;}
    public void addActions(List<Action> actions){
        this.actions.addAll(actions);
    }

    public List<Action> getActions(){
        return actions;
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
        json.put("purchaseHistory", infosToJson(purchaseHistory.getReceipts()));
        json.put("role", role);
        json.put("managerPermissions", fromActionToString(getActions()));
        return json;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
