package utils.userInfoRelated;

import utils.stateRelated.Action;

import java.util.LinkedList;
import java.util.List;

public class Info {
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    private int id;
    private String name;
    private String email;
    private String birthday;
    private int age;

    private List<Action> managerActions;

    public Info(int id, String name, String email, String birthday, int age){
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.age = age;
        managerActions = new LinkedList<>();
    }

    public void addManagerActions(List<Action> actions){
        managerActions.addAll(actions);
    }

    public List<Action> getManagerActions(){
        return managerActions;
    }


}
